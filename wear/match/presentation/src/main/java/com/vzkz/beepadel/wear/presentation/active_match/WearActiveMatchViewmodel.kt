package com.vzkz.beepadel.wear.presentation.active_match


import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.presentation.ui.BaseViewModel
import com.vzkz.match.domain.MatchTracker

class WearActiveMatchViewmodel(
    private val dispatchers: DispatchersProvider,
) :
    BaseViewModel<WearActiveMatchState, WearActiveMatchIntent, WearActiveMatchEvent>(
        WearActiveMatchState.initial,
        dispatchers
    ) {

    override fun reduce(intent: WearActiveMatchIntent) {
//        when (intent) {
//
//        }
    }
}
