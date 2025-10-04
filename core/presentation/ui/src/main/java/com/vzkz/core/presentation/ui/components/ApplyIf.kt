package com.vzkz.core.presentation.ui.components

import androidx.compose.ui.Modifier

inline fun Modifier.applyIf(
    condition: Boolean,
    modifier: Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        this.then(modifier())
    } else this
}