package com.vzkz.beepadel.wear.presentation.active_match.ambient

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.wear.ambient.AmbientLifecycleObserver
import timber.log.Timber

@Composable
fun AmbientObserver(
    onEnterAmbient: (AmbientLifecycleObserver.AmbientDetails) -> Unit,
    onExitAmbientMode: () -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val callback = object : AmbientLifecycleObserver.AmbientLifecycleCallback {
            override fun onEnterAmbient(ambientDetails: AmbientLifecycleObserver.AmbientDetails) {
                super.onEnterAmbient(ambientDetails)

                onEnterAmbient(ambientDetails)
            }

            override fun onExitAmbient() {
                super.onExitAmbient()
                onExitAmbientMode()
            }
        }
        val observer = AmbientLifecycleObserver(context as ComponentActivity, callback)
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

}