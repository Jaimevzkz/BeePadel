package com.vzkz.beepadel.navigation

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.vzkz.beepadel.MainActivity
import com.vzkz.beepadel.settings.presentation.SettingsScreen
import com.vzkz.match.domain.MatchTracker
import com.vzkz.match.presentation.active_match.ActiveMatchScreen
import com.vzkz.match.presentation.active_match.service.ActiveMatchService
import com.vzkz.match.presentation.match_history.MatchHistoryScreen
import kotlinx.coroutines.flow.first
import org.koin.compose.getKoin

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(KeyMatchHistoryScreen)

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
                        MatchHistoryScreen(
                            onNavigateToActiveMatch = {
                                backStack.add(KeyActiveMatchScreen)
                            },
                            onNavigateToSettings = {
                                backStack.add(KeySettingsScreen)
                            }
                        )
                    }
                }

                is KeyActiveMatchScreen -> {
                    NavEntry(
                        key = key
                    ) {
                        val context = LocalContext.current
                        ActiveMatchScreen(
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

                is KeySettingsScreen -> {
                    NavEntry(
                        key = key
                    ) {
                        SettingsScreen()
                    }
                }

                else -> throw RuntimeException("Invalid NavKey")
            }
        }
    )
}
