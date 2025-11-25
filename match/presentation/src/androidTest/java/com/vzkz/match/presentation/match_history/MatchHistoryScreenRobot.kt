package com.vzkz.match.presentation.match_history

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.vzkz.core.presentation.ui.R
import kotlinx.coroutines.runBlocking

class MatchHistoryScreenRobot(
    private val composeActivityRule: ComposeContentTestRule,
    private val context: Context
) {
    fun add_first_match_text_displayed(): MatchHistoryScreenRobot {
        runBlocking {
            composeActivityRule
                .onNodeWithText(context.getString(R.string.create_you_first_match))
                .assertIsDisplayed()
        }

        return this
    }
}