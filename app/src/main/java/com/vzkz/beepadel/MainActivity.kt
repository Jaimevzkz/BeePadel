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
import androidx.lifecycle.lifecycleScope
import com.vzkz.beepadel.navigation.NavigationRoot
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.auth.AuthRepository
import com.vzkz.core.domain.error.Result
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val authRepository by inject<AuthRepository>()
    private val dispatchers by inject<DispatchersProvider>()

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
        val code = intent.data?.getQueryParameter("code")
        code?.let {
            lifecycleScope.launch(dispatchers.io) {
                val result = authRepository.fetchAndSaveRefreshToken(code)
                if (result is Result.Error)
                    Timber.tag("IN-APP").e("Error occurred while fetching refresh token: ${result.error}")
            }
        }
        super.onNewIntent(intent)
    }
}