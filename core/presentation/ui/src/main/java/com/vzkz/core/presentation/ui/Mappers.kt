package com.vzkz.core.presentation.ui

import java.util.Locale
import kotlin.time.Duration

fun Duration.formatted(): String {
    val totalSeconds = inWholeSeconds
    val hours = String.format(Locale.ENGLISH, "%02d", totalSeconds / 3600)
    val minutes = String.format(Locale.ENGLISH, "%02d", totalSeconds % 3600 / 60)
    val seconds = String.format(Locale.ENGLISH, "%02d", totalSeconds % 60)

    return "$hours:$minutes:$seconds"
}
