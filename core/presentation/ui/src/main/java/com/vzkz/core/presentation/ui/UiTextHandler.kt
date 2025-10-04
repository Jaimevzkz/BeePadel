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
        is DataError.Network -> this.asUiText()
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
        DataError.Local.DISK_FULL -> StringResource(R.string.error_disk_full)
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

private fun DataError.Network.asUiText(): UiText{
    return when(this){
        DataError.Network.REQUEST_TIMEOUT ->
            UiText.StringResource(
                R.string.error_request_timeout
            )

        DataError.Network.TOO_MANY_REQUEST ->
            UiText.StringResource(
                R.string.error_too_many_request
            )

        DataError.Network.NO_INTERNET ->
            UiText.StringResource(
                R.string.error_no_internet
            )

        DataError.Network.PAYLOAD_TOO_LARGE ->
            UiText.StringResource(
                R.string.error_payload_too_large
            )

        DataError.Network.SERVER_ERROR ->
            UiText.StringResource(
                R.string.error_server_error
            )

        DataError.Network.SERIALIZATION ->
            UiText.StringResource(
                R.string.error_serialization
            )

        else ->
            UiText.StringResource(
                R.string.error_unknown
            )
    }
}

fun RootError.asUiText(): UiText = when (this) {
    is DataError -> this.asUiText()
    is GenericError -> this.asUiText()
    is ExerciseError -> this.asUiText()
    is MessagingError -> this.asUiText()
}
