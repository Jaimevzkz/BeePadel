package com.vzkz.beepadel.settings.presentation.general_settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.StravaIcon

@Composable
fun ClickableSetting(
    modifier: Modifier = Modifier,
    startIcon: @Composable RowScope.() -> Unit = {},
    title: String,
    description: String? = null,
    endIcon: (@Composable RowScope.() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 12.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        startIcon()
        Column(Modifier.weight(1f)) {
            Text(text = title, color = MaterialTheme.colorScheme.onSurface)
            description?.let {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (endIcon != null) {
            endIcon()
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun PreviewClickableSetting() {
    BeePadelTheme {
        ClickableSetting(
            modifier = Modifier,
            startIcon = {
                Row {
                    Icon(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp),
                        imageVector = StravaIcon,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = null
                    )
                }
            },
            title = "Connect with Strava",
            onClick = {},
            description = "Example small description that could actually get pretty long",
            endIcon = null
        )
    }
}