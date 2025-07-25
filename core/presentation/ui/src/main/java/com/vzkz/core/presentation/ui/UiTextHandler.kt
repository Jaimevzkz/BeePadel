package com.vzkz.core.presentation.ui

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.GenericError
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


private fun DataError.Logic.asUiText(): UiText {
    return when (this) {
       DataError.Logic.EMPTY_SET_LIST -> StringResource(R.string.error_empty_set_list)
    }
}

fun RootError.asUiText(): UiText = when (this) {
    is DataError -> this.asUiText()
    is GenericError -> this.asUiText()
}
