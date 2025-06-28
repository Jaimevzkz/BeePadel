package com.vzkz.match.presentation.match_history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vzkz.common.match
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.match.domain.Match

@Composable
fun MatchCard(
    modifier: Modifier = Modifier,
    match: Match
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(match.setList) { set ->
                Column {
                    Text(
                        text = set.formattedSet.first.toString(),
                        color = if (set.formattedSet.first in 6..7) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = set.formattedSet.second.toString(),
                        color = if (set.formattedSet.second in 6..7) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }


}

@Preview
@Composable
private fun Preview() {
    BeePadelTheme {
        MatchCard(match = match())
    }
}