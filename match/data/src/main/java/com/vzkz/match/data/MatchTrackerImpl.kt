package com.vzkz.match.data

import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.Timer
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.MatchTracker
import com.vzkz.match.domain.model.Match
import com.vzkz.match.domain.model.Points
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration
import com.vzkz.common.general.data_generator.emptySet
import com.vzkz.common.general.data_generator.emptyGame
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class MatchTrackerImpl(
    private val applicationScope: CoroutineScope,
    private val dispatchers: DispatchersProvider,
    private val localStorageRepository: LocalStorageRepository
) : MatchTracker {
    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    override val elapsedTime = _elapsedTime.asStateFlow()

    private val initialMatchState = Match(
        matchId = UUID.randomUUID(),
        setList = listOf(
            emptySet()
        ),
        dateTimeUtc = ZonedDateTime.now(),
        elapsedTime = Duration.ZERO,
    )

    private val _activeMatch = MutableStateFlow(initialMatchState)
    override val activeMatch = _activeMatch.asStateFlow()

    private var previousMatchStateList = mutableListOf(activeMatch.value)

    private val _isMatchPlaying = MutableStateFlow(false)
    override val isMatchPlaying = _isMatchPlaying.asStateFlow()

    init {
        Timer
            .timeAndEmit()
            .onEach { timer ->
                _elapsedTime.value += timer
            }
            .flowOn(dispatchers.default)
            .launchIn(applicationScope)

        isMatchPlaying
            .flatMapLatest { isMatchPlaying ->
                if (isMatchPlaying) {
                    Timer.timeAndEmit()
                } else flowOf()
            }
            .onEach {
                _elapsedTime.value += it
            }
            .launchIn(applicationScope)
    }

    override suspend fun finishMatch(): Result<Unit, DataError.Local> {
        val finalMatch = activeMatch.value.copy(
            elapsedTime = elapsedTime.value
        )
        _activeMatch.update { finalMatch }
        return localStorageRepository.insertOrReplaceMatch(finalMatch)
    }

    override fun addPointToPlayer1() {
        addPointTo(true)
    }

    override fun addPointToPlayer2() {
        addPointTo(false)
    }

    override fun undoPoint() {
        _activeMatch.update { previousMatchStateList.last() }

        if (previousMatchStateList.size > 1)
            previousMatchStateList.removeAt(previousMatchStateList.size - 1)
    }

    override fun discardMatch() {
        _activeMatch.update { initialMatchState }
    }

    private fun addPointTo(isPlayer1: Boolean) {
        val gameToChange = activeMatch.value.setList.last().gameList.last()

        val newGame = gameToChange.addPointTo(isPlayer1)

        val newSetList = activeMatch.value.setList.toMutableList()
        val newGameList = activeMatch.value.setList.last().gameList.toMutableList()

        newGameList[newGameList.size - 1] = newGame

        newSetList[newSetList.size - 1] =
            newSetList[newSetList.size - 1].copy(gameList = newGameList)

        if (newGame.player1Points == Points.Won || newGame.player2Points == Points.Won) {
            if (newSetList.last().getWinner() != null) {
                newSetList.add(
                    emptySet()
                )
            } else {
                newGameList.add(
                    emptyGame()
                )
            }
        }
        previousMatchStateList.add(activeMatch.value)
        _activeMatch.update {
            it.copy(
                setList = newSetList
            )
        }
    }

    fun setPlayingMatch(isPlayingMatch: Boolean) {
        this._isMatchPlaying.value = isPlayingMatch
    }
}