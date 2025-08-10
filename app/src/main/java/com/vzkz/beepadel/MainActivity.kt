package com.vzkz.beepadel

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

class MainActivity : ComponentActivity() {
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
                BuildConfig.VERSION_NAME
            }
        }
    }
}