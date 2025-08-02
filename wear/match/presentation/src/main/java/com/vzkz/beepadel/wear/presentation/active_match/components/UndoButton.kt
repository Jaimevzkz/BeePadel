package com.vzkz.beepadel.wear.presentation.active_match.components


import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedIconButton
import com.vzkz.core.presentation.designsystem.UndoIcon

@Composable
internal fun UndoButton(
    modifier: Modifier = Modifier,
    onUndoPoint: () -> Unit
) {
    OutlinedIconButton(
        modifier = modifier,
        onClick = onUndoPoint,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = UndoIcon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}
