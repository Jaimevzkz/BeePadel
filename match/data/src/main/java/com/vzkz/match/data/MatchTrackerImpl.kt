package com.vzkz.match.data

import com.vzkz.common.general.data_generator.emptyGame
import com.vzkz.common.general.data_generator.emptyMatch
import com.vzkz.common.general.data_generator.emptySet
import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import com.vzkz.core.connectivity.domain.messaging.MessagingAction.Start
import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.core.domain.ZonedDateTimeProvider
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.Timer
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.core.domain.error.UUIDProvider
import com.vzkz.core.domain.error.asEmptyDataResult
import com.vzkz.match.domain.MatchTracker
import com.vzkz.match.domain.WatchConnector
import com.vzkz.match.domain.model.Points
import com.vzkz.match.domain.model.getGameCount
import com.vzkz.match.domain.model.getSetCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration

@OptIn(ExperimentalCoroutinesApi::class)
class MatchTrackerImpl(
    private val applicationScope: CoroutineScope,
    private val dispatchers: DispatchersProvider,
    private val localStorageRepository: LocalStorageRepository,
    private val zonedDateProvider: ZonedDateTimeProvider,
    private val uUIDProvider: UUIDProvider,
    private val watchConnector: WatchConnector
) : MatchTracker {
    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    override val elapsedTime = _elapsedTime.asStateFlow()

    private fun initialMatchState() =
        emptyMatch(
            matchId = uUIDProvider.randomUUID(),
            setId = uUIDProvider.randomUUID(),
            gameId = uUIDProvider.randomUUID(),
            zonedDateTime = zonedDateProvider.now()
        )

    private val _activeMatch = MutableStateFlow(initialMatchState())
    override val activeMatch = _activeMatch.asStateFlow()

    private var previousMatchStateList = mutableListOf(activeMatch.value)

    private val _isTeam1Serving: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    override val isTeam1Serving = _isTeam1Serving.asStateFlow()

    private val _isMatchResumed = MutableStateFlow(false)
    override val isMatchResumed = _isMatchResumed.asStateFlow()

    private val _isMatchStarted = MutableStateFlow(false)
    override val isMatchStarted: Flow<Boolean> = _isMatchStarted.asStateFlow()


    private val heartRateList = isMatchStarted
        .flatMapLatest { hasStarted ->
            if (hasStarted) {
                watchConnector.messagingActions
            } else flowOf()
        }.filterIsInstance<MessagingAction.HeartRateUpdate>()
        .map { it.heartRate }
        .runningFold(initial = emptyList<Int>()) { currentHeartRates, newHeartRate ->
            currentHeartRates + newHeartRate
        }.stateIn(
            applicationScope,
            SharingStarted.Lazily,
            emptyList()
        )

    override val currentHeartRate: Flow<Int?>
        get() = heartRateList
            .flatMapLatest { hearRateList ->
                if (hearRateList.isNotEmpty())
                    flowOf(hearRateList.last())
                else flowOf(null)
            }


    init {
        isMatchResumed
            .flatMapLatest { isMatchPlaying ->
                if (isMatchPlaying) {
                    Timer.timeAndEmit()
                } else flowOf()
            }
            .onEach {
                _elapsedTime.value += it
            }
            .launchIn(applicationScope)

        elapsedTime
            .onEach {
                watchConnector.sendActionToWatch(MessagingAction.TimeUpdate(it))
            }
            .launchIn(applicationScope)

        watchConnector
            .messagingActions
            .onEach { action ->
                when (action) {
                    is Start -> {
                        Timber.i("Receiving start message from watch")
                        setIsTeam1Serving(action.isTeam1Serving)
                        setIsMatchStarted(true)
                        setPlayingMatch(true)
                        applicationScope.launch(dispatchers.default) {
                            Timber.i("Sending start message to watch")
                            watchConnector.sendActionToWatch(Start(action.isTeam1Serving))
                        }
                    }

                    is MessagingAction.AddPointTo -> addPointTo(action.addToTeam1)

                    is MessagingAction.UndoPoint -> undoPoint()

                    else -> Unit
                }
            }
            .flowOn(dispatchers.default)
            .launchIn(applicationScope)
    }

    override suspend fun finishMatch(): Result<Unit, DataError> {
        val finalSetList = if (activeMatch.value.setList.last().getWinner() == null)
            activeMatch.value.setList.toMutableList().dropLast(1)
        else activeMatch.value.setList

        if (finalSetList.isEmpty()) return Result.Error(DataError.Logic.EMPTY_SET_LIST)

        val isHeartRateListEmpty = heartRateList.value.isEmpty()
        val finalMatch = activeMatch.value.copy(
            elapsedTime = elapsedTime.value,
            setList = finalSetList,
            avgHeartRate = if (!isHeartRateListEmpty)
                heartRateList.value.average().toInt() else null,
            maxHeartRate = if (!isHeartRateListEmpty) heartRateList.value.max() else null,
        )

        return when (val insert = localStorageRepository.insertOrReplaceMatch(finalMatch)) {
            is Result.Success -> {
                applicationScope.launch {
                    watchConnector.sendActionToWatch(MessagingAction.Finish)
                    resetMatchTrackerState()
                }

                Result.Success(Unit)
            }

            is Result.Error -> {
                return insert.asEmptyDataResult()
            }
        }
    }

    override fun addPointToPlayer1() {
        addPointTo(true)
    }

    override fun addPointToPlayer2() {
        addPointTo(false)
    }

    override fun undoPoint() {
        _activeMatch.update { previousMatchStateList.last() }
        applicationScope.launch(dispatchers.default) {
            val setList = previousMatchStateList.last().setList
            val gameList = setList.last().gameList
            val points =
                gameList.last().player1Points.ordinal to gameList.last().player2Points.ordinal
            watchConnector.sendActionToWatch(
                MessagingAction.UpdateAfterUndo(
                    points = points,
                    games = gameList.getGameCount(),
                    sets = setList.getSetCount()
                )
            )
        }

        if (previousMatchStateList.size > 1)
            previousMatchStateList.removeAt(previousMatchStateList.size - 1)
    }

    override suspend fun discardMatch() {
        resetMatchTrackerState()
    }

    private fun addPointTo(isPlayer1: Boolean) {
        val gameToChange = activeMatch.value.setList.last().gameList.last()

        val newGame = gameToChange.addPointTo(isPlayer1)

        val newSetList = activeMatch.value.setList.toMutableList()
        val newGameList = activeMatch.value.setList.last().gameList.toMutableList()

        newGameList[newGameList.size - 1] = newGame
        var messageToWatch: MessagingAction =
            MessagingAction.PointsUpdate(newGame.player1Points.ordinal to newGame.player2Points.ordinal)

        newSetList[newSetList.size - 1] =
            newSetList[newSetList.size - 1].copy(gameList = newGameList)

        if (newGame.player1Points == Points.Won || newGame.player2Points == Points.Won) {
            if (newSetList.last().getWinner() != null) { // New set
                newSetList.add(
                    emptySet(
                        setId = uUIDProvider.randomUUID(),
                        gameId = uUIDProvider.randomUUID()
                    )
                )
                messageToWatch = MessagingAction.SetsUpdate(newSetList.getSetCount())
            } else { // new game
                newGameList.add(
                    emptyGame(
                        uuid = uUIDProvider.randomUUID()
                    )
                )
                messageToWatch = MessagingAction.GamesUpdate(newGameList.getGameCount())
            }
            setIsTeam1Serving(!_isTeam1Serving.value!!)
        }
        previousMatchStateList.add(activeMatch.value)
        _activeMatch.update {
            it.copy(
                setList = newSetList
            )
        }
        applicationScope.launch(dispatchers.default) {
            watchConnector.sendActionToWatch(messageToWatch)
        }
    }

    override fun setPlayingMatch(isPlayingMatch: Boolean) {
        this._isMatchResumed.value = isPlayingMatch
    }

    override fun setIsMatchStarted(isMatchStarted: Boolean) {
        _isMatchStarted.value = isMatchStarted
    }

    override fun setIsTeam1Serving(isTeam1Serving: Boolean?) {
        _isTeam1Serving.value = isTeam1Serving
        applicationScope.launch(dispatchers.default) {
            watchConnector.sendActionToWatch(MessagingAction.ServingUpdate(isTeam1Serving))
        }
    }

    private suspend fun resetMatchTrackerState() {
        delay(100L)
        _activeMatch.update { initialMatchState() }
        setPlayingMatch(false)
        setIsMatchStarted(false)
        setIsTeam1Serving(null)
        watchConnector.sendActionToWatch(MessagingAction.Discard)
        previousMatchStateList = mutableListOf(activeMatch.value)
        _elapsedTime.value = Duration.ZERO
    }
}