package com.vzkz.match.presentation.match_history

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MatchHistoryScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var matchHistoryScreenRobot: MatchHistoryScreenRobot
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        matchHistoryScreenRobot = MatchHistoryScreenRobot(composeRule, context)
    }

    @Test
    fun add_first_match_is_displayed_when_data_loaded_and_match_list_empty() {
        composeRule.setContent {
            BeePadelTheme {
                MatchHistoryScreenRoot(
                    state = MatchHistoryState.initial.copy(
                        dataLoaded = true,
                        matchHistory = emptyList()
                    ),
                    onAction = { },
                )
            }
        }
        matchHistoryScreenRobot
            .add_first_match_text_displayed()
    }

}