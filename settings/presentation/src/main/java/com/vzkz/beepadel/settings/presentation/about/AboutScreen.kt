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
import com.vzkz.core.presentation.ui.R

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
                        text = stringResource(R.string.about) + " ${stringResource(R.string.beepadel)}",
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
            Text("Who are we?", style = MaterialTheme.typography.headlineMedium)
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
        append("BeePadel is a pádel tracker app, designed by players, for players.\n")
        append("Behind the development of the app is the Free Open Source Software (FOSS) philosophy. ")
        append("This means that the code for the app can be found publicly on ")

        pushLink(
            LinkAnnotation.Url(
                url = BuildConfig.GITHUB_URL,
            )
        )
        append("GitHub")
        pop()

        append(". It also means that this app will never charge you any kind of licensing fee for its use.\n\n")

        append("This project is currently under active development by indie developers in their free time. ")
        append("We don't want any monetary donations, but we appreciate any other kind of support—")
        append("such as a rating on the Play Store or a GitHub star—that is totally free of charge. ")
        append("This support is really helpful so the project continues growing.")
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