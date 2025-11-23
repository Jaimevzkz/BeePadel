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
import com.vzkz.beepadel.settings.presentation.R
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
            else stringResource(R.string.configure_strava),
        onClick = {
            if (isLoggedIntoStrava) {
                onConfigureStrava()
            } else {
                onLaunchAuthRequest()
            }
        }
    )
}
