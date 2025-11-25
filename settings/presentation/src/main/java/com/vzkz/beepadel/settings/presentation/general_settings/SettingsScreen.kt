@file:OptIn(ExperimentalMaterial3Api::class)

package com.vzkz.beepadel.settings.presentation.general_settings

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vzkz.beepadel.settings.presentation.BuildConfig
import com.vzkz.beepadel.settings.presentation.general_settings.components.AboutButton
import com.vzkz.beepadel.settings.presentation.general_settings.components.BooleanSetting
import com.vzkz.beepadel.settings.presentation.general_settings.components.ClickableSetting
import com.vzkz.beepadel.settings.presentation.general_settings.components.ConnectToStravaButton
import com.vzkz.beepadel.settings.presentation.general_settings.components.ContactButton
import com.vzkz.beepadel.settings.presentation.general_settings.components.GithubStartButton
import com.vzkz.beepadel.settings.presentation.general_settings.components.PlayerStoreButton
import com.vzkz.beepadel.settings.presentation.general_settings.components.SectionTitle
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.components.BeePadelScaffold
import com.vzkz.core.presentation.ui.R
import org.koin.androidx.compose.koinViewModel


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavToConfigureStrava: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)
    val activity = LocalActivity.current
    val context = LocalContext.current

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
        Box(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .scrollable(rememberScrollState(), Orientation.Vertical)
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

                SectionTitle(
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
                AboutButton(onClick = {})
            }

            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                text = "version ${BuildConfig.APP_VERSION_NAME}",
                color = MaterialTheme.colorScheme.onBackground
            )
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