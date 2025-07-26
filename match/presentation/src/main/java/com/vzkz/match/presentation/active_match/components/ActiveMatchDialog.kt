package com.vzkz.match.presentation.active_match.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vzkz.core.presentation.designsystem.components.BeePadelActionButton
import com.vzkz.core.presentation.designsystem.components.BeePadelDialog
import com.vzkz.core.presentation.designsystem.components.BeePadelOutlinedActionButton
import com.vzkz.core.presentation.ui.UiText
import com.vzkz.match.presentation.R
import com.vzkz.match.presentation.active_match.ActiveMatchIntent
import com.vzkz.match.presentation.model.ActiveMatchDialog

@Composable
fun ActiveMatchDialog(
    activeMatchDialogToShow: ActiveMatchDialog,
    insertMatchLoading: Boolean,
    error: UiText?,
    onAction: (ActiveMatchIntent) -> Unit
) {
    val activeDialogTitle: Int
    val onClickIntent: ActiveMatchIntent
    val errorButtonColor: Boolean
    val activeDialogDescription: String?
    val primaryButtonText: String
    when (activeMatchDialogToShow) {
        ActiveMatchDialog.DISCARD_MATCH -> {
            activeDialogTitle = R.string.discard_match_question
            onClickIntent = ActiveMatchIntent.DiscardMatch
            errorButtonColor = true
            activeDialogDescription = null
            primaryButtonText = stringResource(R.string.discard)
        }

        ActiveMatchDialog.FINISH_MATCH -> {
            activeDialogTitle = R.string.end_match_question
            onClickIntent = ActiveMatchIntent.FinishMatch
            errorButtonColor = false
            activeDialogDescription = null
            primaryButtonText = stringResource(R.string.end)
        }

        ActiveMatchDialog.ERROR -> {
            activeDialogTitle = R.string.error_occurred
            onClickIntent = ActiveMatchIntent.DiscardMatch
            errorButtonColor = true
            activeDialogDescription = error?.asString()
            primaryButtonText = stringResource(R.string.discard)
        }
    }

    BeePadelDialog(
        modifier = Modifier,
        title = stringResource(activeDialogTitle),
        description = activeDialogDescription,
        onDismiss = {
            onAction(ActiveMatchIntent.CloseActiveDialog)
        },
        primaryButton = {
            BeePadelActionButton(
                modifier = Modifier.weight(1f),
                text = primaryButtonText,
                isLoading = insertMatchLoading,
                errorButtonColors = errorButtonColor,
                onClick = {
                    onAction(onClickIntent)
                }
            )
        },
        secondaryButton = {
            BeePadelOutlinedActionButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.cancel),
                onClick = {
                    onAction(ActiveMatchIntent.CloseActiveDialog)
                }
            )
        }
    )
}
