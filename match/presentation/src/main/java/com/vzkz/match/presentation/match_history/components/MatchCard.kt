package com.vzkz.match.presentation.match_history.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vzkz.common.match
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.match.domain.Match

@Composable
fun MatchCard(
    modifier: Modifier = Modifier,
    match: Match
) {
    Box(modifier = modifier){
        LazyRow {
            items(match.setList){

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