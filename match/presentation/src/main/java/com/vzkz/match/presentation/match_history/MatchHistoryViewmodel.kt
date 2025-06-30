package com.vzkz.match.presentation.match_history

import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.log.BeeLogger
import com.vzkz.core.presentation.ui.BaseViewModel

class MatchHistoryViewModel(private val dispatchers: DispatchersProvider, private val beeLogger: BeeLogger) :
    BaseViewModel<MatchHistoryState, MatchHistoryIntent, MatchHistoryEvent>(MatchHistoryState.initial, dispatchers, beeLogger) {

    override fun reduce(intent: MatchHistoryIntent) {
        when (intent) {
            is MatchHistoryIntent.NavigateToActiveMatch -> sendEvent(MatchHistoryEvent.NavigateToActiveMatch)
        }
    }

}
