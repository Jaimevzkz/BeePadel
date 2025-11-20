package com.vzkz.beepadel.wear.app.presentation

import timber.log.Timber

class TimberTaggedDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return "TimberWear-${super.createStackElementTag(element)}"
    }
}
