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
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.vzkz.beepadel.MainActivity
import com.vzkz.beepadel.MainViewmodel
import com.vzkz.beepadel.settings.presentation.SettingsScreen
import com.vzkz.core.notification.ActiveMatchService
import com.vzkz.match.domain.MatchTracker
import com.vzkz.match.presentation.active_match.ActiveMatchScreen
import com.vzkz.match.presentation.match_history.MatchHistoryScreen
import kotlinx.coroutines.flow.first
import net.openid.appauth.AuthorizationService
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewmodel = koinViewModel(),
) {
    val backStack = rememberNavBackStack(KeyMatchHistoryScreen)

    val activity = LocalActivity.current
    val intent = activity?.intent

    val deepLinkHandler: DeepLinkHandler = { uri ->
        when {
            uri.toString().contains("active_match") -> {
                KeyActiveMatchScreen
            }

            uri.toString().contains("oauth2redirect") -> {
                Timber.tag("IN-APP").i("received intent in compose: $intent")
                intent?.let {
                    mainViewModel.handleAuthResponseIntent(intent = intent)
                }
                KeySettingsScreen
            }

            else -> null
        }
    }

    LaunchedEffect(Unit) {
        val uri = intent?.data
        uri?.let {
            deepLinkHandler(it)?.let { key ->
                backStack[0] = (key)
            }
        }
    }

    val matchTracker = getKoin().get<MatchTracker>()
    LaunchedEffect(Unit) {
        if (matchTracker.isMatchStarted.first() && backStack.last() == KeyMatchHistoryScreen)
            backStack[0] = KeyActiveMatchScreen
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider =
            entryProvider {
                entry<KeyMatchHistoryScreen> {
                    MatchHistoryScreen(
                        onNavigateToActiveMatch = {
                            backStack[0] = KeyActiveMatchScreen
                        },
                        onNavigateToSettings = {
                            backStack[0] = KeySettingsScreen
                        }
                    )
                }
                entry<KeyActiveMatchScreen> {
                    val context = LocalContext.current
                    ActiveMatchScreen(
                        onNavigateToMatchHistory = {
                            backStack[0] = KeyMatchHistoryScreen
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
                            backStack[0] = KeyMatchHistoryScreen
                        }
                    )
                }
            }
    )
}