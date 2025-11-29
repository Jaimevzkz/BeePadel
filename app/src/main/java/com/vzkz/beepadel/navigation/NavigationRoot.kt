package com.vzkz.beepadel.navigation

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.vzkz.beepadel.MainActivity
import com.vzkz.beepadel.settings.presentation.about.AboutScreen
import com.vzkz.beepadel.settings.presentation.general_settings.SettingsScreen
import com.vzkz.beepadel.settings.presentation.strava_settings.StravaSettingsScreen
import com.vzkz.core.notification.ActiveMatchService
import com.vzkz.match.domain.MatchTracker
import com.vzkz.match.presentation.active_match.ActiveMatchScreen
import com.vzkz.match.presentation.match_history.MatchHistoryScreen
import kotlinx.coroutines.flow.first
import org.koin.compose.getKoin

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(KeyMatchHistoryScreen)

    val activity = LocalActivity.current
    val intent = activity?.intent

    val deepLinkHandler: DeepLinkHandler = { uri ->
        when {
            uri.toString().contains("active_match") -> {
                KeyActiveMatchScreen
            }

            else -> null
        }
    }

    LaunchedEffect(Unit) {
        val uri = intent?.data
        uri?.let {
            deepLinkHandler(it)?.let { key ->
                backStack.add(key)
            }
        }
    }

    val matchTracker = getKoin().get<MatchTracker>()
    LaunchedEffect(Unit) {
        if (matchTracker.isMatchStarted.first() && backStack.last() == KeyMatchHistoryScreen)
            backStack.add(KeyActiveMatchScreen)
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider =
            entryProvider {
                entry<KeyMatchHistoryScreen> {
                    MatchHistoryScreen(
                        onNavigateToActiveMatch = {
                            backStack.add(KeyActiveMatchScreen)
                        },
                        onNavigateToSettings = {
                            backStack.add(KeySettingsScreen)
                        }
                    )
                }
                entry<KeyActiveMatchScreen> {
                    val context = LocalContext.current
                    ActiveMatchScreen(
                        onNavigateToMatchHistory = {
                            backStack.removeLastOrNull()
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
                entry<KeySettingsScreen> {
                    SettingsScreen(
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        },
                        onNavToConfigureStrava = {
                            backStack.add(KeyStravaSettingsScreen)
                        },
                        onNavToAbout = {
                            backStack.add(KeyAboutScreen)
                        }
                    )
                }
                entry<KeyStravaSettingsScreen> {
                    StravaSettingsScreen(
                        onNavBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
                entry<KeyAboutScreen> {
                    AboutScreen(
                        onNavBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
            }
    )
}