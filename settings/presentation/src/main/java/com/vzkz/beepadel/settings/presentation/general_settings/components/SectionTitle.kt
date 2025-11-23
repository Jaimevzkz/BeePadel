package com.vzkz.beepadel.settings.presentation.general_settings.components


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.padding(vertical = 8.dp),
        text = text,
        style = MaterialTheme.typography.headlineSmall
    )
}
