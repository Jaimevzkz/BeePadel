package com.vzkz.beepadel.wear.presentation.active_match.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ClickableArea(
    modifier: Modifier = Modifier,
    onAddPointToTeam1: () -> Unit,
    onAddPointToTeam2: () -> Unit,
) {
    Row(modifier = modifier.fillMaxSize()) {
        Box(
            Modifier
                .weight(1f)
                .clickable {
                    onAddPointToTeam1()
                }
        )

        Spacer(Modifier.width(2.dp))

        Box(
            Modifier
                .weight(1f)
                .clickable {
                    onAddPointToTeam2()
                }
        )
    }
}
