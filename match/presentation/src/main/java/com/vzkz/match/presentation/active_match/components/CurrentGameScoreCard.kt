package com.vzkz.match.presentation.active_match.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vzkz.core.presentation.designsystem.BallIcon
import com.vzkz.core.presentation.designsystem.BeePadelGold
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.Exo2
import com.vzkz.match.domain.model.Points
import com.vzkz.match.presentation.R
import com.vzkz.match.presentation.util.formatted
import timber.log.Timber
import kotlin.time.Duration

@Composable
fun CurrentGameScoreCard(
    modifier: Modifier = Modifier,
    ownPoints: Points,
    otherPoints: Points,
    currentOwnGames: Int,
    currentOtherGames: Int,
    isServing: Boolean?,
    elapsedTime: Duration,
    goldenPoint: Boolean
) {
    val pointsFontSize = 60.sp
    val gameFontSize = 30.sp
    val context = LocalContext.current
    val shouldShowGoldenPoint =  (goldenPoint && ownPoints == Points.Forty && otherPoints == Points.Forty)

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .semantics {
                    contentDescription = context.getString(R.string.elapsed_time)
                }
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            text = elapsedTime.formatted(),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 30.sp
        )
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .then(
                    if (isServing == true || shouldShowGoldenPoint)
                        Modifier.border(
                            2.dp, Brush.horizontalGradient(
                                listOf(
                                    if (shouldShowGoldenPoint) BeePadelGold else MaterialTheme.colorScheme.onPrimary,
                                    Color.Transparent
                                )
                            ), RoundedCornerShape(8.dp)
                        )
                    else Modifier
                )
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            Color.Transparent
                        )
                    )
                )

                .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text(
                modifier = Modifier,
                text = ownPoints.string,
                fontSize = pointsFontSize,
                fontFamily = Exo2,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.size(24.dp))
            Text(
                modifier = Modifier,
                text = currentOwnGames.toString(),
                fontSize = gameFontSize,
                fontFamily = Exo2,
                color = MaterialTheme.colorScheme.onPrimary
            )
            if (isServing == true) {
                Spacer(Modifier.size(12.dp))
                Icon(
                    imageVector = BallIcon,
                    contentDescription = stringResource(R.string.own_player_serving),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .then(
                    if (isServing == false || shouldShowGoldenPoint)
                        Modifier.border(
                            2.dp, Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    if (shouldShowGoldenPoint) BeePadelGold else MaterialTheme.colorScheme.onSecondary
                                )
                            ), RoundedCornerShape(8.dp)
                        )
                    else Modifier
                )
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.secondary,
                        )
                    )
                )
                .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End

        ) {
            if (isServing == false) {
                Icon(
                    imageVector = BallIcon,
                    contentDescription = stringResource(R.string.other_player_serving),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(Modifier.size(12.dp))
            }
            Text(
                modifier = Modifier,
                text = currentOtherGames.toString(),
                fontSize = gameFontSize,
                fontFamily = Exo2,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(Modifier.size(24.dp))
            Text(
                modifier = Modifier,
                text = otherPoints.string,
                fontSize = pointsFontSize,
                fontFamily = Exo2,
                color = MaterialTheme.colorScheme.onSecondary
            )

        }
    }
}

@Preview
@Composable
private fun ScoreCardPreview() {
    BeePadelTheme {
        CurrentGameScoreCard(
            modifier = Modifier,
            ownPoints = Points.Forty,
            otherPoints = Points.Forty,
            currentOwnGames = 0,
            currentOtherGames = 0,
            isServing = true,
            elapsedTime = Duration.ZERO,
            goldenPoint = true,
//            goldenPoint = false,
        )
    }
}