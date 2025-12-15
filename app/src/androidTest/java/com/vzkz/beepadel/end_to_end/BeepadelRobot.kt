package com.vzkz.beepadel.end_to_end

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.vzkz.beepadel.MainActivity
import com.vzkz.common.general.R
import kotlinx.coroutines.runBlocking

class BeepadelRobot(
    private val activityRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    private val context: Context
) {

    fun assert_on_match_history_screen(): BeepadelRobot {
        runBlocking {
            activityRule.waitUntil(
                timeoutMillis = 2000L,
                condition = {
                    activityRule
                        .onNodeWithText(context.getString(R.string.beepadel))
                        .isDisplayed()
                }
            )
        }
        return this
    }

    fun navigate_to_active_match(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithContentDescription(context.getString(R.string.start_match))
                .performClick()
        }

        return this
    }

    fun assert_in_active_match_screen(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithText(context.getString(R.string.who_starts_serving))
                .assertIsDisplayed()
        }
        return this
    }

    fun click_start_on_serving_dialog(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithText(context.getString(R.string.start))
                .performClick()
        }
        return this
    }

    fun add_point_to(addToTeam1: Boolean, numberOfPointsToAdd: Int = 1): BeepadelRobot {
        runBlocking {
            val contentDescriptionToSearch =
                if (addToTeam1) R.string.add_own_point else R.string.add_other_point
            (0..<numberOfPointsToAdd).forEach { _ ->
                activityRule
                    .onNodeWithContentDescription(context.getString(contentDescriptionToSearch))
                    .performClick()
            }
        }
        return this
    }

    fun click_on_discard_match(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithContentDescription(context.getString(R.string.discard_match))
                .performClick()
        }
        return this
    }

    fun click_on_cancel(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithText(context.getString(R.string.cancel))
                .performClick()
        }
        return this
    }

    fun click_discard(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithText(context.getString(R.string.discard))
                .performClick()
        }
        return this
    }

    fun click_on_finish_match(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithContentDescription(context.getString(R.string.end_match))
                .performClick()
        }
        return this
    }

    fun confirm_finish_match(): BeepadelRobot {
        runBlocking {
            activityRule
                .onNodeWithText(context.getString(R.string.end))
                .performClick()
        }
        return this
    }

    fun assert_dialog_is_displayed(title: String, description: String? = null): BeepadelRobot{
        runBlocking {
            activityRule
                .onNodeWithText(title)
                .assertIsDisplayed()

            description?.let {
                activityRule
                    .onNodeWithText(it)
                    .assertIsDisplayed()
            }
        }
        return this
    }
}