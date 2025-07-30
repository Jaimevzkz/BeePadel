package com.vzkz.beepadel.wear.presentation.active_match

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.FilledTonalIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedIconButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.vzkz.beepadel.designsystem_wear.BeePadelTheme
import com.vzkz.beepadel.wear.presentation.R
import com.vzkz.core.presentation.designsystem.BallIcon
import com.vzkz.core.presentation.designsystem.Exo2
import com.vzkz.core.presentation.designsystem.FinishIcon
import com.vzkz.core.presentation.designsystem.PlusOneIcon
import com.vzkz.core.presentation.designsystem.UndoIcon
import com.vzkz.match.domain.model.Points
import com.vzkz.match.presentation.util.formatted
import org.koin.androidx.compose.koinViewModel

@Composable
fun WearActiveMatchScreenRoot(
    viewModel: WearActiveMatchViewmodel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(events) {
        when (events) {
            null -> {}
            else -> {}
        }
    }

    WearActiveMatchScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun WearActiveMatchScreen(
    state: WearActiveMatchState,
    onAction: (WearActiveMatchIntent) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalIconButton(
            onClick = {},
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Icon(
                imageVector = FinishIcon,
                contentDescription = ""
            )
        }

        WearScoreCard(
            pointsTeam1 = state.pointsTeam1,
            gamesTeam1 = state.gamesTeam1,
            pointsTeam2 = state.pointsTeam2,
            gamesTeam2 = state.gamesTeam2,
            isTeam1Serving = state.isTeam1Serving
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            val roundedCorner = 8.dp
            val width = 14.dp
            val height = 6.dp
            val horizontalSpacing = 2.dp
            (0..<state.setsTeam1).forEach { _ ->
                Box(
                    Modifier
                        .clip(RoundedCornerShape(roundedCorner))
                        .size(width = width, height = height)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(Modifier.width(horizontalSpacing))
            }

            Spacer(Modifier.weight(1f))
            (0..<state.setsTeam2).forEach { _ ->
                Box(
                    Modifier
                        .clip(RoundedCornerShape(roundedCorner))
                        .size(width = width, height = height)
                        .background(MaterialTheme.colorScheme.secondary)
                )
                Spacer(Modifier.width(horizontalSpacing))
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            Text(text = state.elapsedTime.formatted(), color = MaterialTheme.colorScheme.onSurface)
        }

        UndoButton()
    }
}

@Composable
private fun WearScoreCard(
    pointsTeam1: Points,
    gamesTeam1: Int,
    pointsTeam2: Points,
    gamesTeam2: Int,
    isTeam1Serving: Boolean?
) {
    val fontSize = 28.sp
    val servingIconPlaceholder = 16.dp
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = pointsTeam1.string,
                color = MaterialTheme.colorScheme.primary,
                fontSize = fontSize,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = Exo2,
            )

            if (isTeam1Serving == true)
                Icon(
                    modifier = Modifier.size(16.dp).padding(start = 4.dp),
                    imageVector = BallIcon,
                    contentDescription = stringResource(com.vzkz.match.presentation.R.string.own_player_serving),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            else
                Spacer(Modifier.width(servingIconPlaceholder))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            val smallFontSize = 18.sp
            Text(
                text = gamesTeam1.toString(),
                color = MaterialTheme.colorScheme.primary,
                fontSize = smallFontSize,
                fontFamily = Exo2,
            )

            Spacer(Modifier.size(8.dp))

            Text(
                text = gamesTeam2.toString(),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = smallFontSize,
                fontFamily = Exo2,
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isTeam1Serving == false)
                Icon(
                    modifier = Modifier.size(16.dp).padding(end = 4.dp),
                    imageVector = BallIcon,
                    contentDescription = stringResource(com.vzkz.match.presentation.R.string.other_player_serving),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            else
                Spacer(Modifier.width(servingIconPlaceholder))
            Text(
                text = pointsTeam2.string,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = fontSize,
                fontFamily = Exo2,
            )


        }


    }
}


@Composable
fun UndoButton(
    modifier: Modifier = Modifier,
) {
    OutlinedIconButton(
        modifier = modifier,
        onClick = {},
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = UndoIcon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}


@WearPreviewDevices
@Composable
private fun WearActiveMatchScreenPreview() {
    BeePadelTheme {
        WearActiveMatchScreen(
            state = WearActiveMatchState.initial.copy(
                setsTeam1 = 3,
                setsTeam2 = 2,
                isTeam1Serving = true
            ),
            onAction = {}
        )
    }
}
