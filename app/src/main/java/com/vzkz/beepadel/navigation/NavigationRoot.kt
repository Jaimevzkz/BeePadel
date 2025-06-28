package com.vzkz.beepadel.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.vzkz.match.presentation.active_match.ActiveMatchScreenRot
import com.vzkz.match.presentation.match_history.MatchHistoryScreenRot
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(KeyMatchHistoryScreen)
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
                            onNavigateToActiveMatch = { matchId ->
                                backStack.add(KeyActiveMatchScreen(matchId))
                            }
                        )
                    }
                }

                is KeyActiveMatchScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        ActiveMatchScreenRot(
                            viewModel = koinViewModel{
                                parametersOf(key.matchId)
                            },
                        )
                    }
                }

                else -> throw RuntimeException("Invalid NavKey")
            }
        }
    )
}
