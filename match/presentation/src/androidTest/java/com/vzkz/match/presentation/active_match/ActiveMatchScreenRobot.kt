package com.vzkz.match.presentation.active_match

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import kotlinx.coroutines.runBlocking

class ActiveMatchScreenRobot(
    private val composeActivityRule: ComposeContentTestRule,
    private val context: Context
) {

    fun assertAllContentDescriptionsAreDisplayed(contentDescriptionList: List<Int>): ActiveMatchScreenRobot {

        runBlocking {
            contentDescriptionList.forEach { contentDescriptionList ->
                composeActivityRule
                    .onNodeWithContentDescription(context.getString(contentDescriptionList))
                    .assertIsDisplayed()
            }
        }

        return this
    }
}