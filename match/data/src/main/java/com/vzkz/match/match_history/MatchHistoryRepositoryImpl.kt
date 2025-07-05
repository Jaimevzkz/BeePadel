package com.vzkz.match.match_history


import com.vzkz.core.database.domain.GameDataSource
import com.vzkz.core.database.domain.MatchDataSource
import com.vzkz.core.database.domain.SetDataSource
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.Result
import com.vzkz.core.domain.error.asEmptyDataResult
import com.vzkz.match.data.util.toDomain
import com.vzkz.match.domain.match_history.MatchHistoryRepository
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class MatchHistoryRepositoryImpl(
    private val matchDataSource: MatchDataSource,
    private val setDataSource: SetDataSource,
    private val gameDataSource: GameDataSource,
    private val dispatchers: DispatchersProvider
) : MatchHistoryRepository {

    override fun getMatchHistory(): Flow<List<Match>> {
        return matchDataSource.getMatchListAsFlow().map { matchEntityModelList ->
            matchEntityModelList.map { matchEntityModel ->
                val setList = setDataSource.getSetListForMatch(matchEntityModel.matchId)
                    .map { setEntityModel ->
                        val gameList = gameDataSource.getGameListForSet(setEntityModel.setId)
                            .map { it.toDomain() }

                        setEntityModel.toDomain(gameList)
                    }

                matchEntityModel.toDomain(setList)
            }
        }
    }

    override suspend fun deleteMatch(matchId: UUID): EmptyResult<DataError.Local> {
        return withContext(dispatchers.io) {
            val deleteGamesDeferredList = mutableListOf<Deferred<Result<Unit, DataError.Local>>>()
            setDataSource.getSetIdList(matchId).forEach { setId ->
                deleteGamesDeferredList.add(async { gameDataSource.deleteGamesWithSetId(setId) })
            }

            val deleteSetsDeferred = async { setDataSource.deleteSetsWithMatchId(matchId) }

            val deleteMatchDeferred = async { matchDataSource.deleteMatchById(matchId) }

            deleteGamesDeferredList.forEach { deferred ->
                val gamesResult = deferred.await()
                if (gamesResult is Result.Error)
                    gamesResult.asEmptyDataResult()
            }
            val setsResult = deleteSetsDeferred.await()
            if (setsResult is Result.Error)
                setsResult.asEmptyDataResult()

            val matchResult = deleteMatchDeferred.await()
            if (matchResult is Result.Error)
                matchResult.asEmptyDataResult()

            Result.Success(Unit)
        }
    }
}