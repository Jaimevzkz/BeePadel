package com.vzkz.match.presentation.match_history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vzkz.common.general.data_generator.dummyMatch
import com.vzkz.core.presentation.designsystem.BeePadelGold
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.CalendarIcon
import com.vzkz.core.presentation.designsystem.TrophyIcon
import com.vzkz.match.presentation.R
import com.vzkz.match.presentation.match_history.model.MatchUi
import com.vzkz.match.presentation.util.toMatchUi

@Composable
fun MatchCard(
    modifier: Modifier = Modifier,
    match: MatchUi,
    onDeleteMatch: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
            .padding(16.dp),
    ) {
        MatchDurationSection(
            isMatchWon = match.isMatchWon, elapsedTime = match.elapsedTime,
            onDeleteMatch = onDeleteMatch
        )

        SetOverviewSection(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.8f),
            setList = match.formatedSetList
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )

        MatchDateSection(
            dateTime = match.dateTimeFormatted
        )
    }
}

@Composable
private fun MatchDurationSection(
    modifier: Modifier = Modifier,
    isMatchWon: Boolean,
    elapsedTime: String,
    onDeleteMatch: () -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.match_duration),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = elapsedTime,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.weight(1f))
        if (isMatchWon) {
            Icon(imageVector = TrophyIcon, contentDescription = stringResource(R.string.match_won), tint = BeePadelGold)
        }
        Box() {
            DropdownMenu(
                expanded = isDropdownExpanded,
                containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            isDropdownExpanded = false
                            onDeleteMatch()
                        }
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_match),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.delete_match),
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
            IconButton(onClick = { isDropdownExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.match_options),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }


    }
}

@Composable
private fun SetOverviewSection(
    modifier: Modifier = Modifier,
    setList: List<Pair<Int, Int>>
) {
    val fontSize = 22.sp
    Row(
        modifier = modifier
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        setList.forEach { set ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = set.first.toString(),
                    fontSize = fontSize,
                    color =
                        if (set.first in 6..7 && set.second != 7) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = set.second.toString(),
                    fontSize = fontSize,
                    color =
                        if (set.second in 6..7 && set.first != 7) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
private fun MatchDateSection(
    modifier: Modifier = Modifier,
    dateTime: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = CalendarIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = dateTime,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun Preview() {
    BeePadelTheme {
        MatchCard(
            match = dummyMatch().toMatchUi(),
            modifier = Modifier,
            onDeleteMatch = {}
        )
    }
}