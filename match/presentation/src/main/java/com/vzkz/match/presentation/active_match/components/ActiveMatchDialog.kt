package com.vzkz.match.presentation.active_match.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vzkz.core.presentation.designsystem.components.BeePadelActionButton
import com.vzkz.core.presentation.designsystem.components.BeePadelDialog
import com.vzkz.core.presentation.designsystem.components.BeePadelOutlinedActionButton
import com.vzkz.common.general.R
import com.vzkz.core.presentation.ui.UiText
import com.vzkz.match.presentation.model.ActiveMatchDialog

@Composable
fun ActiveMatchDialog(
    activeMatchDialogToShow: ActiveMatchDialog,
    insertMatchLoading: Boolean,
    error: UiText?,
    onCloseActiveDialog: () -> Unit,
    onFinishMatch: () -> Unit,
    onDiscardMatch: () -> Unit,
) {
    val activeDialogTitle: Int
    val onClickIntent: () -> Unit
    val errorButtonColor: Boolean
    val activeDialogDescription: String?
    val primaryButtonText: String
    when (activeMatchDialogToShow) {
        ActiveMatchDialog.DISCARD_MATCH -> {
            activeDialogTitle = R.string.discard_match_question
            onClickIntent = onDiscardMatch
            errorButtonColor = true
            activeDialogDescription = null
            primaryButtonText = stringResource(R.string.discard)
        }

        ActiveMatchDialog.FINISH_MATCH -> {
            activeDialogTitle = R.string.end_match_question
            onClickIntent = onFinishMatch
            errorButtonColor = false
            activeDialogDescription = null
            primaryButtonText = stringResource(R.string.end)
        }

        ActiveMatchDialog.ERROR -> {
            activeDialogTitle = R.string.error_occurred
            onClickIntent = onDiscardMatch
            errorButtonColor = true
            activeDialogDescription = error?.asString()
            primaryButtonText = stringResource(R.string.discard)
        }
    }

    BeePadelDialog(
        modifier = Modifier,
        title = stringResource(activeDialogTitle),
        description = activeDialogDescription,
        onDismiss = onCloseActiveDialog,
        primaryButton = {
            BeePadelActionButton(
                modifier = Modifier.weight(1f),
                text = primaryButtonText,
                isLoading = insertMatchLoading,
                errorButtonColors = errorButtonColor,
                onClick = onClickIntent

            )
        },
        secondaryButton = {
            BeePadelOutlinedActionButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.cancel),
                onClick = onCloseActiveDialog
            )
        }
    )
}
