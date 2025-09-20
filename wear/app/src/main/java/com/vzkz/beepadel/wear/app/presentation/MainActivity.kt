/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.vzkz.beepadel.wear.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.vzkz.beepadel.designsystem_wear.BeePadelTheme
import com.vzkz.beepadel.wear.presentation.active_match.WearActiveMatchScreenRoot
import com.vzkz.core.notification.ActiveMatchService
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            BeePadelTheme {
                WearActiveMatchScreenRoot(
                    onServiceToggle = { shouldStartService ->
                        if (shouldStartService){
                            startService(
                                ActiveMatchService.createStartIntent(
                                    context = applicationContext,
                                    activityClass = this::class.java
                                )
                            )
                        }
                        else{
                            startService(
                                ActiveMatchService.createStopIntent(context = applicationContext)
                            )
                        }
                    }
                )
            }
        }
    }
}