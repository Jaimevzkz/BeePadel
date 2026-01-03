package com.vzkz.beepadel.settings.presentation.general_settings.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vzkz.common.general.R
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
        startIcon = {
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
                        contentDescription = stringResource(R.string.configure_strava)
                    )
            }
        },
        title =
            if (isLoggedIntoStrava) stringResource(R.string.configure_strava)
            else stringResource(R.string.connect_with_strava),
        description = if (!isLoggedIntoStrava) stringResource(R.string.every_time_you_finish_a_match_it_will_be_automatically_uploaded_to_strava_as_a_new_activity) else stringResource(
            R.string.tweak_how_your_matches_will_appear_on_strava
        ),
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
        startIcon = {
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
        description = stringResource(R.string.a_github_star_helps_this_open_source_project_more_visibility),
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
        startIcon = {
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
        description = stringResource(R.string.leaving_a_review_in_the_google_play_store_helps_way_more_than_one_would_think_making_the_app_appear_higher_in_the_search_list),
        onClick = onClick
    )
}

@Composable
fun ContactButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ClickableSetting(
        modifier = modifier,
        startIcon = {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp),
                imageVector = Icons.Default.Mail,
                contentDescription = stringResource(R.string.contact_us)
            )
        },
        title =
            stringResource(R.string.contact_us),
        description = stringResource(R.string.reach_us_with_any_kind_of_feedback_bug_reports_feature_suggestion_or_just_some_kind_words),
        onClick = onClick
    )
}

@Composable
fun AboutButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val text = stringResource(R.string.about2) + " ${stringResource(R.string.beepadel)}"
    ClickableSetting(
        modifier = modifier,
        startIcon = {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp),
                imageVector = Icons.Default.Info,
                contentDescription = text
            )
        },
        title = text,
        description = stringResource(R.string.some_context_on_what_this_project_is),
        onClick = onClick
    )
}

@Composable
fun ImportExportMatchesButton(
    modifier: Modifier = Modifier,
    onImport: () -> Unit,
    onExport: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 12.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(20.dp),
            imageVector = Icons.Default.ImportExport,
            contentDescription = stringResource(R.string.import_export_matches)
        )

        Column(Modifier.weight(1f)) {
            Text(text = stringResource(R.string.import_export_matches), color = MaterialTheme.colorScheme.onSurface)
                Text(
                    text = stringResource(R.string.backup_your_data_to_a_json_file_or_import_it_from_one_previously_created),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
        }

        Box() {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Open dropdown"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Upload,
                            contentDescription = stringResource(R.string.import_matches)
                        )
                    },
                    text = { Text(stringResource(R.string.import_matches)) },
                    onClick = {
                        expanded = false
                        onImport()
                    }
                )
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = stringResource(R.string.export_matches)
                        )
                    },
                    text = { Text(stringResource(R.string.export_matches)) },
                    onClick = {
                        expanded = false
                        onExport()
                    }
                )
            }
        }

    }
}
