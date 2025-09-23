package com.vzkz.beepadel.end_to_end

import android.Manifest
import android.os.Build
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.vzkz.beepadel.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class EndToEndTesting {
    @get:Rule
    val activityRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val grantPermissionRule: TestRule =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
        else TestRule { base, _ -> base }

    @Test
    fun testStartingAMatchAndDiscardingIt() {
        BeepadelRobot(activityRule, ApplicationProvider.getApplicationContext())
            .navigateToActiveMatch()
            .assertInActiveMatchScreen()
            .clickStartOnServingDialog()
            .addPointTo(true, 1)
            .clickOnDiscardMatch()
            .confirmDiscardMatch()
            .assertOnMatchHistoryScreen()
    }

    @Test
    fun testStartingAMatchAndFinishingAfterASet() = runBlocking<Unit> {
        BeepadelRobot(activityRule, ApplicationProvider.getApplicationContext())
            .navigateToActiveMatch()
            .assertInActiveMatchScreen()
            .clickStartOnServingDialog()
            .addPointTo(true, 24)
            .clickOnFinishMatch()
            .confirmFinishMatch()
            .assertOnMatchHistoryScreen()
    }
}