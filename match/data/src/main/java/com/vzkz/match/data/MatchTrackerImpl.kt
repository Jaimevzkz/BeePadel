package com.vzkz.match.data

import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.Timer
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.MatchTracker
import com.vzkz.match.domain.model.Game
import com.vzkz.match.domain.model.Match
import com.vzkz.match.domain.model.Points
import com.vzkz.match.domain.model.Set
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
            Set(
                UUID.randomUUID(), listOf(
                    Game(
                        gameId = UUID.randomUUID(),
                        player1Points = Points.Zero,
                        player2Points = Points.Zero
                    )
                )
            )
        ),
        dateTimeUtc = ZonedDateTime.now(),
        elapsedTime = Duration.ZERO,
    )

    private val _activeMatch = MutableStateFlow(initialMatchState)
    override val activeMatch = _activeMatch.asStateFlow()

    private val previousMatchState = activeMatch.value

    init {
        Timer
            .timeAndEmit()
            .onEach { timer ->
                _elapsedTime.value += timer
            }
            .flowOn(dispatchers.default)
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
        _activeMatch.update { previousMatchState }
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

        if (newGame.player1Points == Points.Won || newGame.player2Points == Points.Won) {
            if (activeMatch.value.setList.last().getWinner() != null) {
                newSetList.add(
                    Set(
                        setId = UUID.randomUUID(),
                        gameList = emptyList()
                    )
                )
            } else {
                newGameList.add(
                    Game(
                        gameId = UUID.randomUUID(),
                        player1Points = Points.Zero,
                        player2Points = Points.Zero
                    )
                )
            }
        }
        newSetList[newSetList.size - 1] =
            newSetList[newSetList.size - 1].copy(gameList = newGameList)
        _activeMatch.update {
            it.copy(
                setList = newSetList
            )
        }
    }
}