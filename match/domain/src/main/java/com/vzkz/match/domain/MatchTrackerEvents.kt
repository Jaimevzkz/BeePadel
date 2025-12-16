package com.vzkz.match.domain

sealed class MatchTrackerEvents {
    data object MatchStartedFromWatch : MatchTrackerEvents()
}