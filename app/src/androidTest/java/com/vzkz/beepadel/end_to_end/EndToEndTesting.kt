package com.vzkz.beepadel.end_to_end

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.vzkz.beepadel.MainActivity
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.presentation.ui.asUiText
import kotlinx.coroutines.runBlocking
import org.junit.Before
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

    private lateinit var beepadelRobot: BeepadelRobot
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        beepadelRobot = BeepadelRobot(activityRule, context)
    }

    @Test
    fun clicking_finish_match_displays_the_correct_dialogs() = runBlocking<Unit> {
        beepadelRobot
            .navigate_to_active_match()
            .assert_in_active_match_screen()
            .click_start_on_serving_dialog()
            .add_point_to(true, 3)
            .click_on_finish_match()
            .assert_dialog_is_displayed(title = context.getString(com.vzkz.match.presentation.R.string.end_match_question))
            .confirm_finish_match()
            .assert_dialog_is_displayed(
                title = context.getString(com.vzkz.match.presentation.R.string.error_occurred),
                description =  DataError.Logic.EMPTY_SET_LIST.asUiText().asString(context)
            )
            .click_discard()
    }

    @Test
    fun starting_a_match_and_discarding_it() {
        beepadelRobot
            .navigate_to_active_match()
            .assert_in_active_match_screen()
            .click_start_on_serving_dialog()
            .add_point_to(true, 1)
            .click_on_discard_match()
            .click_discard()
            .assert_on_match_history_screen()
    }

    @Test
    fun starting_a_match_and_finishing_after_a_set() = runBlocking<Unit> {
        beepadelRobot
            .navigate_to_active_match()
            .assert_in_active_match_screen()
            .click_start_on_serving_dialog()
            .add_point_to(true, 24)
            .click_on_finish_match()
            .confirm_finish_match()
            .assert_on_match_history_screen()
    }
}