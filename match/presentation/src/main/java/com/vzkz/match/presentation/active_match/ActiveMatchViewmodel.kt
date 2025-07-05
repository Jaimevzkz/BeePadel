package com.vzkz.match.presentation.active_match

import com.vzkz.common.general.data_generator.match
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.Result
import com.vzkz.core.presentation.ui.BaseViewModel
import com.vzkz.match.domain.active_match.ActiveMatchRepository

class ActiveMatchViewmodel(
    private val dispatchers: DispatchersProvider,
    private val activeMatchRepository: ActiveMatchRepository
) :
    BaseViewModel<ActiveMatchState, ActiveMatchIntent, ActiveMatchEvent>(ActiveMatchState.initial, dispatchers) {

    override fun reduce(intent: ActiveMatchIntent) {
        when (intent) {
            ActiveMatchIntent.FinishMatch -> finishMatch()
        }
    }

    private fun finishMatch(){
        ioLaunch {
            when(val insert = activeMatchRepository.insertMatch(match = match())){
                is Result.Success -> sendEvent(ActiveMatchEvent.NavToHistoryScreen)
                is Result.Error -> {
                    //todo handle errors
                }
            }

        }

    }
}
