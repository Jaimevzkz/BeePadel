package com.vzkz.match.presentation.active_match.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vzkz.core.presentation.designsystem.Exo2
import com.vzkz.core.presentation.designsystem.PlusOneIcon
import com.vzkz.core.presentation.designsystem.UndoIcon
import com.vzkz.match.presentation.R

@Composable
fun ControlsSection(
    modifier: Modifier = Modifier,
    ownSets: Int,
    otherSets: Int,
    onAddOwnPoint: () -> Unit,
    onAddOtherPoint: () -> Unit,
    onUndo: () -> Unit,
) {
    val setFontSize = 36.sp
    val iconButtonPadding = 16.dp
    val iconSize = 40.dp
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onAddOwnPoint() }
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(iconButtonPadding)
            ) {
                Icon(
                    modifier = Modifier
                        .size(iconSize),
                    imageVector = PlusOneIcon,
                    contentDescription = stringResource(R.string.add_own_point),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Box(
                modifier = Modifier
                    .border(
                        width = 3.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "$ownSets - $otherSets",
                    fontSize = setFontSize,
                    fontFamily = Exo2
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onAddOtherPoint() }
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(iconButtonPadding)
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = PlusOneIcon,
                    contentDescription = stringResource(R.string.add_own_point),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }

        }
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = CircleShape
                )
                .padding(iconButtonPadding),
            onClick = { onUndo() }) {
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = UndoIcon,
                contentDescription = stringResource(R.string.undo),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }

}
