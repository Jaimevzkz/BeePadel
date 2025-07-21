package com.vzkz.match.presentation.active_match

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vzkz.common.general.data_generator.match
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.Result
import com.vzkz.core.presentation.ui.BaseViewModel
import com.vzkz.match.domain.MatchTracker
import com.vzkz.match.domain.active_match.ActiveMatchRepository
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ActiveMatchViewmodel(
    private val dispatchers: DispatchersProvider,
    private val activeMatchRepository: ActiveMatchRepository,
    private val matchTracker: MatchTracker
) :
    BaseViewModel<ActiveMatchState, ActiveMatchIntent, ActiveMatchEvent>(
        ActiveMatchState.initial,
        dispatchers
    ) {

    init {
        matchTracker
            .elapsedTime
            .onEach { elapsedTime ->
                _state.update { it.copy(elapsedTime = elapsedTime) }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)

    }

    override fun reduce(intent: ActiveMatchIntent) {
        when (intent) {
            ActiveMatchIntent.FinishMatch -> finishMatch()
            ActiveMatchIntent.AddOtherPoint -> {}
            ActiveMatchIntent.AddOwnPoint -> {}
            ActiveMatchIntent.DiscardMatch -> {}
            ActiveMatchIntent.EndMatch -> {}
            ActiveMatchIntent.UndoPoint -> {}
        }
    }

    private fun finishMatch() { //todo impl
        ioLaunch {
            when (val insert = activeMatchRepository.insertMatch(match = match())) {
                is Result.Success -> sendEvent(ActiveMatchEvent.NavToHistoryScreen)
                is Result.Error -> {
                    //todo handle errors
                }
            }

        }

    }
}
