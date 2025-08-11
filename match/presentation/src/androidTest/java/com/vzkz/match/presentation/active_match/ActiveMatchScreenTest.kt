package com.vzkz.match.presentation.active_match

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.match.presentation.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ActiveMatchScreenTest {
    @get:Rule
    val composeRule = createComposeRule() // Starts a component in isolation

    private lateinit var activeMatchScreenRobot: ActiveMatchScreenRobot
    private lateinit var context: Context

    @Before
    fun setUp(){
        context = ApplicationProvider.getApplicationContext()
        activeMatchScreenRobot = ActiveMatchScreenRobot(composeRule, context)
    }

    @Test
    fun testInitialScreenState() {
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
//                    R.string.beepadel, // test failing action
                    //todo complete
                )
            )

    }

}