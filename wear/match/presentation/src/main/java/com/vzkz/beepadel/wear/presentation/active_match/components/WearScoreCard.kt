package com.vzkz.beepadel.wear.presentation.active_match.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.FilledTonalIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import androidx.wear.compose.material3.OutlinedIconButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TextButton
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
import kotlin.time.Duration

@Composable
internal fun WearScoreCard(
    pointsTeam1: Points,
    gamesTeam1: Int,
    pointsTeam2: Points,
    gamesTeam2: Int,
    setsTeam1: Int,
    setsTeam2: Int,
    elapsedTime: Duration,
    isTeam1Serving: Boolean?
) {
    Column {
        val fontSize = 28.sp
        val servingIconPlaceholder = 16.dp
        //
        val roundedCorner = 8.dp
        val width = 14.dp
        val height = 6.dp
        val horizontalSpacing = 2.dp
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
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
                            modifier = Modifier
                                .size(16.dp)
                                .padding(start = 4.dp),
                            imageVector = BallIcon,
                            contentDescription = stringResource(com.vzkz.match.presentation.R.string.own_player_serving),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    else
                        Spacer(Modifier.width(servingIconPlaceholder))
                }
                Row {
                    (0..<setsTeam1).forEach { _ ->
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(roundedCorner))
                                .size(width = width, height = height)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(Modifier.width(horizontalSpacing))
                    }
                }
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
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isTeam1Serving == false)
                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .padding(end = 4.dp),
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
                Row {
                    (0..<setsTeam2).forEach { _ ->
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(roundedCorner))
                                .size(width = width, height = height)
                                .background(MaterialTheme.colorScheme.secondary)
                        )
                        Spacer(Modifier.width(horizontalSpacing))
                    }
                }
            }
        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = elapsedTime.formatted(),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

    }
}
