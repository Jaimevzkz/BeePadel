package com.vzkz.match.presentation.active_match

import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.presentation.ui.BaseViewModel

class ActiveMatchViewmodel(
    private val matchId: Int,
    private val dispatchers: DispatchersProvider,
) :
    BaseViewModel<ActiveMatchState, ActiveMatchIntent, ActiveMatchEvent>(ActiveMatchState.initial, dispatchers) {

    override fun reduce(intent: ActiveMatchIntent) {
        when (intent) {
            else -> {}
        }
    }

    init {
        //do something with match id
    }
}
