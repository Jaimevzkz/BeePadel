package com.vzkz.beepadel.end_to_end

import android.Manifest
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.vzkz.beepadel.MainActivity
import org.junit.Rule
import org.junit.Test

class EndToEndTesting {
    @get:Rule
    val activityRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    @Test
    fun testMainScreenShowsData() {
        BeepadelRobot(activityRule, ApplicationProvider.getApplicationContext())
            .navigateToActiveMatch()
            .assertInActiveMatchScreen()
    }
}