package com.vzkz.active_match.presentation

import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.log.BeeLogger
import com.vzkz.core.presentation.ui.BaseViewModel

class ActiveMatchViewmodel(private val dispatchers: DispatchersProvider, private val beeLogger: BeeLogger) :
    BaseViewModel<ActiveMatchState, ActiveMatchIntent, ActiveMatchEvent>(ActiveMatchState.initial, dispatchers, beeLogger) {

    override fun reduce(intent: ActiveMatchIntent) {
        when (intent) {
            else -> {}
        }
    }
}
