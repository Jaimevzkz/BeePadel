package com.vzkz.beepadel

import timber.log.Timber

class TimberTaggedDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return "Timber-${super.createStackElementTag(element)}"
    }
}
