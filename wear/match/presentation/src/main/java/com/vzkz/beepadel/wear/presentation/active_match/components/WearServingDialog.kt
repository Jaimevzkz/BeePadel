package com.vzkz.beepadel.wear.presentation.active_match.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedIconButton
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.RadioButtonDefaults
import androidx.wear.compose.material3.Text
import com.vzkz.common.general.R
import com.vzkz.core.presentation.designsystem.StartIcon

@Composable
fun WearServingDialog(
    modifier: Modifier = Modifier,
    onStartMatch: (isTeam1Serving: Boolean) -> Unit
) {
    var servingTeam1 by remember { mutableStateOf(true) }
    val radioButtonColors = RadioButtonDefaults.radioButtonColors(
        selectedControlColor = MaterialTheme.colorScheme.onPrimary,
        selectedContentColor = MaterialTheme.colorScheme.onPrimary,
        selectedContainerColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
        unselectedContainerColor = MaterialTheme.colorScheme.surfaceContainer
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically)
    ) {
        Text(
            modifier = Modifier.padding(top = 28.dp),
            text = stringResource(R.string.who_starts_serving),
            color = MaterialTheme.colorScheme.onBackground
        )
        RadioButton(
            modifier = Modifier.height(34.dp),
            selected = servingTeam1,
            onSelect = { servingTeam1 = true },
            label = { Text(stringResource(R.string.team_1)) },
            colors = radioButtonColors
        )

        RadioButton(
            modifier = Modifier.height(34.dp),
            selected = !servingTeam1,
            onSelect = { servingTeam1 = false },
            label = { Text(stringResource(R.string.team_2)) },
            colors = radioButtonColors.copy(
                selectedControlColor = MaterialTheme.colorScheme.onSecondary,
                selectedContentColor = MaterialTheme.colorScheme.onSecondary,
                selectedContainerColor = MaterialTheme.colorScheme.secondary,
            )
        )

        OutlinedIconButton(
            onClick = { onStartMatch(servingTeam1) },
            modifier = Modifier
        ) {
            Icon(
                imageVector = StartIcon,
                contentDescription = stringResource(id = R.string.start_match),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
