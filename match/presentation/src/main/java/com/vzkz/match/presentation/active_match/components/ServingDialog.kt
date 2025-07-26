package com.vzkz.match.presentation.active_match.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vzkz.core.presentation.designsystem.components.BeePadelActionButton
import com.vzkz.core.presentation.designsystem.components.BeePadelDialog
import com.vzkz.core.presentation.designsystem.components.BeePadelOutlinedActionButton
import com.vzkz.match.presentation.R

@Composable
fun ServingDialog(
    modifier: Modifier = Modifier,
    onStartMatch: (team1Serving: Boolean) -> Unit,
    onCancel: () -> Unit
) {
    var team1Serving by remember { mutableStateOf(true) }
    BeePadelDialog(
        modifier = modifier,
        title = stringResource(R.string.who_starts_serving),
        onDismiss = {},
        body = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    RadioButton(
                        selected = team1Serving,
                        onClick = { team1Serving = true },
                    )
                    Text(
                        text = stringResource(R.string.team_1),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    RadioButton(
                        selected = !team1Serving,
                        onClick = { team1Serving = false },
                    )
                    Text(
                        text = stringResource(R.string.team_2),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        },
        primaryButton = {
            BeePadelActionButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.start),
                isLoading = false,
                onClick = {
                    onStartMatch(team1Serving)
                }
            )
        },
        secondaryButton = {
            BeePadelOutlinedActionButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.cancel),
                isLoading = false,
                onClick = {
                    onCancel()
                }
            )
        }
    )
}
