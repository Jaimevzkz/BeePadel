package com.vzkz.match.presentation.active_match

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.ui.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ActiveMatchScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var activeMatchScreenRobot: ActiveMatchScreenRobot
    private lateinit var context: Context

    @Before
    fun setUp(){
        context = ApplicationProvider.getApplicationContext()
        activeMatchScreenRobot = ActiveMatchScreenRobot(composeRule, context)
    }

    @Test
    fun test_initial_screen_state() {
        composeRule.setContent {
            BeePadelTheme {
                ActiveMatchScreenRoot(
                    state = ActiveMatchState.initial,
                    onServiceToggle = {},
                    onAction = {}
                )
            }
        }
        activeMatchScreenRobot
            .assertAllContentDescriptionsAreDisplayed(
                contentDescriptionList = listOf(
                    //Top section
                    R.string.discard_match,
                    R.string.end_match,
                    // Score board
                    R.string.elapsed_time,
                    R.string.team_1_score,
                    R.string.team_2_score,
                    R.string.team_2_score,
                    // Controls
                    R.string.add_own_point,
                    R.string.add_other_point,
                    R.string.undo,
                )
            )
    }
}