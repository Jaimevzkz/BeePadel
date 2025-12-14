package com.vzkz.beepadel.settings.presentation.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.components.BeePadelScaffold
import com.vzkz.core.presentation.ui.BuildConfig
import com.vzkz.common.general.R

@Composable
fun AboutScreen(onNavBack: () -> Unit) {
    AboutScreenRoot(onNavBack = onNavBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreenRoot(onNavBack: () -> Unit) {
    BeePadelScaffold(
        topAppBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.about2) + " ${stringResource(R.string.beepadel)}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent
                )
            )
        },
        withGradient = false
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(R.string.who_are_we),
                style = MaterialTheme.typography.headlineMedium
            )
            AboutText()

            Spacer(Modifier.weight(1f))
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = "Version: ${BuildConfig.APP_VERSION_NAME}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun AboutText(modifier: Modifier = Modifier) {

    val annotatedText = buildAnnotatedString {
        append(stringResource(R.string.beepadel_is_a_p_del_tracker_app_designed_by_players_for_players))
        append(stringResource(R.string.behind_the_development_of_the_app_is_the_free_open_source_software_foss_philosophy))
        append(stringResource(R.string.this_means_that_the_code_for_the_app_can_be_found_publicly_on))

        pushLink(
            LinkAnnotation.Url(
                url = BuildConfig.GITHUB_URL,
            )
        )
        append("GitHub")
        pop()

        append(stringResource(R.string.it_also_means_that_this_app_will_never_charge_you_any_kind_of_licensing_fee_for_its_use))

        append(stringResource(R.string.this_project_is_currently_under_active_development_by_indie_developers_in_their_free_time))
        append(stringResource(R.string.we_don_t_want_any_monetary_donations_but_we_appreciate_any_other_kind_of_support))
        append(stringResource(R.string.such_as_a_rating_on_the_play_store_or_a_github_star_that_is_totally_free_of_charge))
        append(stringResource(R.string.this_support_is_really_helpful_so_the_project_continues_growing))
    }

    Text(
        modifier = modifier,
        text = annotatedText,
    )
}

@Preview
@Composable
private fun AboutScreenPreview() {
    BeePadelTheme {
        AboutScreenRoot(
            onNavBack = {}
        )
    }
}