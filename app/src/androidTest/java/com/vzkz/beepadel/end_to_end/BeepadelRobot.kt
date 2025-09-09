package com.vzkz.beepadel.end_to_end

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.vzkz.beepadel.MainActivity
import com.vzkz.match.presentation.R
import kotlinx.coroutines.runBlocking

class BeepadelRobot(
    private val activityRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    private val context: Context
) {

    fun navigateToActiveMatch(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithContentDescription(context.getString(R.string.start_match))
                .performClick()
        }

        return this
    }

    fun assertInActiveMatchScreen(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithText(context.getString(R.string.who_starts_serving))
                .assertIsDisplayed()
        }
        return this
    }
}