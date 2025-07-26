package com.vzkz.beepadel.navigation

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.vzkz.beepadel.MainActivity
import com.vzkz.match.presentation.active_match.ActiveMatchScreenRot
import com.vzkz.match.presentation.active_match.service.ActiveMatchService
import com.vzkz.match.presentation.match_history.MatchHistoryScreenRot
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(KeyMatchHistoryScreen)

    val viewModelStoreOwner = LocalViewModelStoreOwner.current

    val deepLinkHandler: DeepLinkHandler = { uri ->

        when {
            uri.toString().contains("active_match") -> {
                KeyActiveMatchScreen
            }

            else -> null
        }
    }

    val activity = LocalActivity.current
    val intent = activity?.intent
    LaunchedEffect(Unit) {
        val uri = intent?.data
        uri?.let {
            deepLinkHandler(it)?.let { key ->
                Timber.tag("IN-APP").i("Adding screen to backstack, key: $key")
                backStack.add(key)
            }
        }
    }
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                KeyMatchHistoryScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        MatchHistoryScreenRot(
                            onNavigateToActiveMatch = {
                                backStack.add(KeyActiveMatchScreen)
                            }
                        )
                    }
                }

                is KeyActiveMatchScreen -> {
                    NavEntry(
                        key = key
                    ) {
                        val context = LocalContext.current
                        ActiveMatchScreenRot(
                            onNavigateToMatchHistory = {
                                backStack.removeLast()

                            },
                            onServiceToggle = { shouldServiceRun ->
                                if (shouldServiceRun) {
                                    context.startService(
                                        ActiveMatchService.createStartIntent(
                                            context = context,
                                            activityClass = MainActivity::class.java
                                        )
                                    )
                                } else {
                                    context.startService(
                                        ActiveMatchService.createStopIntent(context)
                                    )
                                }

                            }
                        )
                    }
                }

                else -> throw RuntimeException("Invalid NavKey")
            }
        }
    )
}
