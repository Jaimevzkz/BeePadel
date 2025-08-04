package com.vzkz.core.presentation.ui

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.ExerciseError
import com.vzkz.core.domain.error.GenericError
import com.vzkz.core.domain.error.MessagingError
import com.vzkz.core.domain.error.RootError
import com.vzkz.core.presentation.ui.UiText.StringResource

private fun DataError.asUiText(): UiText {
    return when (this) {
        is DataError.Local -> this.asUiText()
        is DataError.Logic -> this.asUiText()
    }
}

private fun GenericError.asUiText(): UiText {
    return when (this) {
        GenericError.UNKNOWN_ERROR -> StringResource(R.string.error_unknown)
    }
}

private fun DataError.Local.asUiText(): UiText {
    return when (this) {
        DataError.Local.INSERT_MATCH_FAILED -> StringResource(R.string.error_insert_match)
        DataError.Local.INSERT_SET_FAILED -> StringResource(R.string.error_insert_set)
        DataError.Local.INSERT_GAME_FAILED -> StringResource(R.string.error_insert_game)
        DataError.Local.DELETE_MATCH_FAILED -> StringResource(R.string.error_delete_match)
        DataError.Local.DELETE_SET_FAILED -> StringResource(R.string.error_delete_set)
        DataError.Local.DELETE_GAME_FAILED -> StringResource(R.string.error_delete_game)
    }
}

private fun ExerciseError.asUiText(): UiText {
    return when (this) {
        ExerciseError.TRACKING_NOT_SUPPORTED -> StringResource(R.string.error_heart_rate_tracking_not_supported)
        ExerciseError.ONGOING_OWN_EXERCISE -> StringResource(R.string.error_ongoing_own_exercise)
        ExerciseError.ONGOING_OTHER_EXERCISE -> StringResource(R.string.error_ongoing_own_exercise)
        ExerciseError.EXERCISE_ALREADY_ENDED -> StringResource(R.string.error_exercise_already_ended)
        ExerciseError.UNKNOWN -> StringResource(R.string.error_unknown)
    }
}


private fun MessagingError.asUiText(): UiText {
    return when (this) {
        MessagingError.CONNECTION_INTERRUPTED ->  StringResource(R.string.error_connection_interrupted)
        MessagingError.DISCONNECTED ->  StringResource(R.string.error_disconnected)
        MessagingError.UNKNOWN ->  StringResource(R.string.error_unknown)
    }
}

private fun DataError.Logic.asUiText(): UiText {
    return when (this) {
        DataError.Logic.EMPTY_SET_LIST -> StringResource(R.string.error_empty_set_list)
    }
}

fun RootError.asUiText(): UiText = when (this) {
    is DataError -> this.asUiText()
    is GenericError -> this.asUiText()
    is ExerciseError -> this.asUiText()
    is MessagingError -> this.asUiText()
}
