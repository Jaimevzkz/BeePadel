package com.vzkz.beepadel.end_to_end

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.vzkz.beepadel.MainActivity
import com.vzkz.match.presentation.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class BeepadelRobot(
    private val activityRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    private val context: Context
) {

    fun assertOnMatchHistoryScreen(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithText(context.getString(R.string.beepadel))
                .assertIsDisplayed()
        }
        return this
    }

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

    fun clickStartOnServingDialog(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithText(context.getString(R.string.start))
                .performClick()
        }
        return this
    }

    fun addPointTo(addToTeam1: Boolean, numberOfPointsToAdd: Int = 1): BeepadelRobot {
        runBlocking {
            val contentDescriptionToSearch = if (addToTeam1) R.string.add_own_point else R.string.add_other_point
            (0..<numberOfPointsToAdd).forEach { _ ->
                activityRule
                    .onNodeWithContentDescription(context.getString(contentDescriptionToSearch))
                    .performClick()
            }
        }
        return this
    }

    fun clickOnDiscardMatch(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithContentDescription(context.getString(R.string.discard_match))
                .performClick()
        }
        return this
    }

    fun confirmDiscardMatch(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithText(context.getString(R.string.discard))
                .performClick()
        }
        return this
    }
}