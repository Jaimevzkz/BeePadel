package com.vzkz.core.database

import com.vzkz.core.database.data.BeePadelDB
import com.vzkz.core.database.data.GameDataSource
import com.vzkz.core.database.data.MatchDataSource
import com.vzkz.core.database.data.SetDataSource
import com.vzkz.core.database.data.util.toDomain
import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.core.domain.error.asEmptyDataResult
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class SqlDelightRepository(
    private val localDB: BeePadelDB,
    private val matchDataSource: MatchDataSource,
    private val setDataSource: SetDataSource,
    private val gameDataSource: GameDataSource,
    private val dispatchers: DispatchersProvider
) : LocalStorageRepository {

    override fun getMatchHistory(): Flow<List<Match>> {
        return matchDataSource.getMatchListAsFlow().map { matchEntityList ->
            matchEntityList.map { matchEntity ->
                val setList = setDataSource.getSetListForMatch(matchEntity.matchId)
                    .map { setEntity ->
                        val gameList = gameDataSource.getGameListForSet(setEntity.setId)
                            .map { it.toDomain() }
                        setEntity.toDomain(gameList)
                    }
                matchEntity.toDomain(setList)
            }
        }
    }

    override suspend fun insertOrReplaceMatch(match: Match): Result<Unit, DataError.Local> {
        return withContext(dispatchers.io) {
            try {
                localDB.transaction {
                    val insertMatch = matchDataSource.insertOrReplaceMatch(
                        matchId = match.matchId,
                        dateTimeUtc = match.dateTimeUtc,
                        elapsedTime = match.elapsedTime
                    )
                    if (insertMatch is Result.Error)
                        insertMatch.asEmptyDataResult()

                    match.setList.forEach { set ->
                        val insertSet = setDataSource.insertOrReplaceSetForMatch(
                            setId = set.setId,
                            matchId = match.matchId
                        )
                        if (insertSet is Result.Error)
                            insertSet.asEmptyDataResult()

                        set.gameList.forEach { game ->
                            val insertGame = gameDataSource.insertOrReplaceGameForSet(
                                gameId = game.gameId,
                                setId = set.setId,
                                serverPoints = game.serverPoints,
                                receiverPoints = game.receiverPoints
                            )
                            if (insertGame is Result.Error)
                                insertGame.asEmptyDataResult()
                        }

                    }
                }
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(DataError.Local.INSERT_MATCH_FAILED)
            }
        }
    }

    override suspend fun deleteMatch(matchId: UUID): Result<Unit, DataError.Local> {
        return withContext(dispatchers.io) {
            val deleteGamesDeferredList = mutableListOf<Deferred<Result<Unit, DataError.Local>>>()
            setDataSource.getSetIdList(matchId).forEach { setId ->
                deleteGamesDeferredList.add(async { gameDataSource.deleteGamesWithSetId(setId) })
            }

            deleteGamesDeferredList.forEach { deferred ->
                val gamesResult = deferred.await()
                if (gamesResult is Result.Error)
                    gamesResult.asEmptyDataResult()
            }

            val setsResult = setDataSource.deleteSetsWithMatchId(matchId)
            if (setsResult is Result.Error)
                setsResult.asEmptyDataResult()

            val matchResult = matchDataSource.deleteMatchById(matchId)
            if (matchResult is Result.Error)
                matchResult.asEmptyDataResult()

            Result.Success(Unit)
        }
    }
}