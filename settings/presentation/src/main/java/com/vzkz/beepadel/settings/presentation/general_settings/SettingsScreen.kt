@file:OptIn(ExperimentalMaterial3Api::class)

package com.vzkz.beepadel.settings.presentation.general_settings

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vzkz.beepadel.settings.presentation.general_settings.components.AboutButton
import com.vzkz.beepadel.settings.presentation.general_settings.components.BooleanSetting
import com.vzkz.beepadel.settings.presentation.general_settings.components.ConnectToStravaButton
import com.vzkz.beepadel.settings.presentation.general_settings.components.ContactButton
import com.vzkz.beepadel.settings.presentation.general_settings.components.GithubStartButton
import com.vzkz.beepadel.settings.presentation.general_settings.components.PlayerStoreButton
import com.vzkz.beepadel.settings.presentation.general_settings.components.SectionTitle
import com.vzkz.common.general.BuildConfig
import com.vzkz.common.general.R
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.components.BeePadelScaffold
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavToConfigureStrava: () -> Unit,
    onNavToAbout: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)
    val activity = LocalActivity.current
    val context = LocalContext.current
    val exportLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.CreateDocument("application/json")
        ) { uri ->
            Timber.tag("IN-APP").i("launcher triggered with uri: $uri")
            uri?.let {
                val stream = context.contentResolver.openOutputStream(it)
                if (stream != null) {
                    viewModel.onAction(SettingsIntent.ExportMatchData(stream))
                }
            }
        }

    BackHandler {
        onNavigateBack()
    }

    LaunchedEffect(events) {
        when (events) {
            SettingsEvent.NavigateBack -> {
                onNavigateBack()
            }

            is SettingsEvent.LaunchAuthRequestIntent -> {
                activity?.startActivity((events as SettingsEvent.LaunchAuthRequestIntent).intent)
            }

            SettingsEvent.ConfigureStrava -> {
                onNavToConfigureStrava()
            }

            SettingsEvent.OpenGithub -> {
                context.startActivity(Intent(Intent.ACTION_VIEW, BuildConfig.GITHUB_URL.toUri()))
            }

            SettingsEvent.OpenPlayStore -> {
                val packageName = context.packageName
                try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        "market://details?id=$packageName".toUri()
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (_: Exception) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        "https://play.google.com/store/apps/details?id=$packageName".toUri()
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }

            SettingsEvent.ContactUs -> {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:${BuildConfig.CONTACT_EMAIL}".toUri()
                }
                context.startActivity(intent)
            }

            SettingsEvent.NavigateToAbout -> {
                onNavToAbout()
            }

            SettingsEvent.SelectExportLauncher -> {
                exportLauncher.launch(BuildConfig.EXPORT_MATCHES_FILE_NAME)
            }

            is SettingsEvent.MakeToast -> {
                Toast.makeText(
                    context,
                    (events as SettingsEvent.MakeToast).uiText.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            null -> {}
        }
    }

    SettingsScreenRoot(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun SettingsScreenRoot(
    state: SettingsState,
    onAction: (SettingsIntent) -> Unit
) {
    BeePadelScaffold(
        topAppBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(SettingsIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent
                ),

                )
        },
        withGradient = false
    ) { innerPadding ->
        val itemSpacing = 8.dp
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {
            SectionTitle(
                modifier = Modifier,
                text = stringResource(R.string.features)
            )

            BooleanSetting(
                modifier = Modifier,
                title = stringResource(R.string.golden_point),
                value = state.goldenPoint,
                onValueChange = { onAction(SettingsIntent.ToggleGoldenPoint) }
            )

            SectionTitle( //todo commented until approved by strava
                modifier = Modifier,
                text = stringResource(R.string.connect)
            )

            ConnectToStravaButton(
                modifier = Modifier,
                isLoggedIntoStrava = state.isLoggedIntoStrava,
                onConfigureStrava = {
                    onAction(SettingsIntent.ConfigureStrava)
                },
                onLaunchAuthRequest = {
                    onAction(SettingsIntent.LaunchAuthRequestIntent)
                }
            )

            SectionTitle(
                modifier = Modifier,
                text = stringResource(R.string.data)
            )
            ImportExportMatchesButton(
                modifier = Modifier,
                onImport = {},
                onExport = { onAction(SettingsIntent.OnExportMatchDataClick) },
            )

            SectionTitle(
                modifier = Modifier,
                text = stringResource(R.string.support)
            )
            GithubStartButton(onClick = { onAction(SettingsIntent.OpenGithub) })
            Spacer(Modifier.height(itemSpacing))
            PlayerStoreButton(onClick = { onAction(SettingsIntent.OpenPlayStore) })

            SectionTitle(
                modifier = Modifier,
                text = stringResource(R.string.about)
            )
            ContactButton(onClick = { onAction(SettingsIntent.ContactUs) })
            Spacer(Modifier.height(itemSpacing))
            AboutButton(onClick = { onAction(SettingsIntent.NavigateToAbout) })
        }
    }
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
            .height(80.dp)
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
        Text(
            text = stringResource(R.string.import_export_matches),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.weight(1f))
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

@Preview
@Composable
private fun SettingsScreenPreview() {
    BeePadelTheme {
        SettingsScreenRoot(
            state = SettingsState.initial.copy(goldenPoint = true, isLoggedIntoStrava = true),
            onAction = {}
        )
    }
}