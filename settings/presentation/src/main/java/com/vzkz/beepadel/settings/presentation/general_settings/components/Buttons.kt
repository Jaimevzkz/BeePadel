package com.vzkz.beepadel.settings.presentation.general_settings.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vzkz.core.presentation.ui.R
import com.vzkz.core.presentation.designsystem.GithubIcon
import com.vzkz.core.presentation.designsystem.PlayStoreIcon
import com.vzkz.core.presentation.designsystem.StravaIcon

@Composable
fun ConnectToStravaButton(
    modifier: Modifier = Modifier,
    isLoggedIntoStrava: Boolean,
    onConfigureStrava: () -> Unit,
    onLaunchAuthRequest: () -> Unit

) {
    ClickableSetting(
        modifier = modifier,
        icon = {
            Row {
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(20.dp),
                    imageVector = StravaIcon,
                    contentDescription = stringResource(R.string.connect_with_strava)
                )

                if (isLoggedIntoStrava)
                    Icon(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp),
                        imageVector = Icons.Default.Check,
                        tint = Color.Green,
                        contentDescription = stringResource(R.string.connected_to_strava)
                    )
            }
        },
        title =
            if (isLoggedIntoStrava) stringResource(R.string.connected_to_strava)
            else stringResource(R.string.connect_with_strava),
        onClick = {
            if (isLoggedIntoStrava) {
                onConfigureStrava()
            } else {
                onLaunchAuthRequest()
            }
        }
    )
}

@Composable
fun GithubStartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit

) {
    ClickableSetting(
        modifier = modifier,
        icon = {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp),
                imageVector = GithubIcon,
                contentDescription = stringResource(R.string.github_star)
            )
        },
        title =
            stringResource(R.string.github_star),
        onClick = onClick
    )
}

@Composable
fun PlayerStoreButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit

) {
    ClickableSetting(
        modifier = modifier,
        icon = {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp),
                imageVector = PlayStoreIcon,
                contentDescription = stringResource(R.string.leave_a_review)
            )
        },
        title =
            stringResource(R.string.leave_a_review),
        onClick = onClick
    )
}
