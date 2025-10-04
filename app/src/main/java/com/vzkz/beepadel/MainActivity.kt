package com.vzkz.beepadel

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.vzkz.beepadel.navigation.NavigationRoot
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import kotlinx.coroutines.flow.StateFlow
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.android.inject
import timber.log.Timber
import kotlin.getValue
import kotlin.time.Duration

class MainActivity : ComponentActivity() {

    private val authService by inject<AuthorizationService>()
    private val mainViewmodel by inject<MainViewmodel>()

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeePadelTheme {
                NavigationRoot(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        Timber.tag("IN-APP").i("Received intent in main activity: $intent")
        super.onNewIntent(intent)
    }


    override fun onDestroy() {
        authService.dispose()
        super.onDestroy()
    }
}