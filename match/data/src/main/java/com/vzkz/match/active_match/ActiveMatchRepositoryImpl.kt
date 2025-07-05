package com.vzkz.match.active_match

import com.vzkz.core.database.domain.GameDataSource
import com.vzkz.core.database.domain.MatchDataSource
import com.vzkz.core.database.domain.SetDataSource
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.active_match.ActiveMatchRepository
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ActiveMatchRepositoryImpl(
    private val matchDataSource: MatchDataSource,
    private val setDataSource: SetDataSource,
    private val gameDataSource: GameDataSource,
    private val dispatchers: DispatchersProvider
): ActiveMatchRepository {
    override suspend fun insertMatch(match: Match): Result<Unit, DataError.Local> {
        return withContext(dispatchers.io){
            matchDataSource.insertOrReplaceMatch(
                matchId = match.matchId,
                dateTimeUtc = match.dateTimeUtc,
                elapsedTime = match.elapsedTime
            )
            match.setList.forEach { set ->
                setDataSource.insertOrReplaceSetForMatch(
                    setId = set.setId,
                    matchId = match.matchId
                )
                set.gameList.forEach { game ->
                    gameDataSource.insertOrReplaceGameForSet(
                        gameId = game.gameId,
                        setId = set.setId,
                        serverPoints = game.serverPoints,
                        receiverPoints = game.receiverPoints
                    )
                }

            }
            Result.Success(Unit)
        }
    }
}