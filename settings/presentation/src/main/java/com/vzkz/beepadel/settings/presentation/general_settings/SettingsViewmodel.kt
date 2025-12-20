package com.vzkz.beepadel.settings.presentation.general_settings

import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.vzkz.beepadel.core.preferences.domain.PreferencesRepository
import com.vzkz.beepadel.settings.presentation.general_settings.SettingsEvent.ConfigureStrava
import com.vzkz.beepadel.settings.presentation.general_settings.SettingsEvent.ContactUs
import com.vzkz.beepadel.settings.presentation.general_settings.SettingsEvent.LaunchAuthRequestIntent
import com.vzkz.beepadel.settings.presentation.general_settings.SettingsEvent.NavigateBack
import com.vzkz.beepadel.settings.presentation.general_settings.SettingsEvent.NavigateToAbout
import com.vzkz.beepadel.settings.presentation.general_settings.SettingsEvent.OpenGithub
import com.vzkz.beepadel.settings.presentation.general_settings.SettingsEvent.OpenPlayStore
import com.vzkz.beepadel.settings.presentation.general_settings.SettingsEvent.SelectExportLauncher
import com.vzkz.common.general.GOLDEN_POINT
import com.vzkz.common.general.R
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.ImportExportRepository
import com.vzkz.core.domain.auth.AuthRepository
import com.vzkz.core.domain.error.Result
import com.vzkz.core.presentation.ui.BaseViewModel
import com.vzkz.core.presentation.ui.BuildConfig
import com.vzkz.core.presentation.ui.UiText
import com.vzkz.core.presentation.ui.asUiText
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.io.OutputStream

class SettingsViewModel(
    private val dispatchers: DispatchersProvider,
    private val preferencesRepository: PreferencesRepository,
    private val authRepository: AuthRepository,
    private val importExportRepository: ImportExportRepository
) : BaseViewModel<SettingsState, SettingsIntent, SettingsEvent>(
    SettingsState.initial,
    dispatchers
) {
    init {
        authRepository.isLoggedIn
            .onEach { isLoggedIn ->
                _state.update { it.copy(isLoggedIntoStrava = isLoggedIn) }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)

        preferencesRepository
            .getBooleanPreferenceAsFlow(GOLDEN_POINT.KEY)
            .onEach { newValue ->
                _state.update {
                    it.copy(goldenPoint = newValue ?: GOLDEN_POINT.DEFAULT_VAL)
                }
            }
            .flowOn(dispatchers.io)
            .launchIn(viewModelScope)

    }

    override fun reduce(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.NavigateBack -> sendEvent(NavigateBack)

            SettingsIntent.ToggleGoldenPoint -> ioLaunch {
                preferencesRepository.storeBooleanPreference(
                    GOLDEN_POINT.KEY, !state.value.goldenPoint
                )
            }

            SettingsIntent.LaunchAuthRequestIntent ->
                sendEvent(LaunchAuthRequestIntent(createAuthRequestIntent()))

            SettingsIntent.ConfigureStrava -> sendEvent(ConfigureStrava)

            SettingsIntent.OpenGithub -> sendEvent(OpenGithub)

            SettingsIntent.OpenPlayStore -> sendEvent(OpenPlayStore)

            SettingsIntent.ContactUs -> sendEvent(ContactUs)

            SettingsIntent.NavigateToAbout -> sendEvent((NavigateToAbout))

            is SettingsIntent.ExportMatchData -> exportMatchData(intent.outputStream)

            SettingsIntent.OnExportMatchDataClick -> sendEvent(SelectExportLauncher)
        }
    }

    private fun exportMatchData(outputStream: OutputStream) {
        ioLaunch {
            val resultStringRes =
                when (val export = importExportRepository.exportData(outputStream)) {
                    is Result.Success -> {

                        Timber.tag("IN-APP").i("Success exporting")
                        UiText.StringResource(R.string.match_data_exported_successfully)
                    }
                    is Result.Error -> {
                        Timber.tag("IN-APP").i("Error exporting")
                        export.error.asUiText()
                    }
                }

            sendEvent(SettingsEvent.MakeToast(resultStringRes))
        }
    }

    private fun createAuthRequestIntent(): Intent {
        val scope = "activity:read,activity:write"
        val clientId = BuildConfig.STRAVA_CLIENT_ID
        val redirectUri = "beepadel://oauth2redirect"


        val intentUri = "https://www.strava.com/oauth/mobile/authorize".toUri()
            .buildUpon()
            .appendQueryParameter("client_id", clientId.toString())
            .appendQueryParameter("redirect_uri", redirectUri)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("approval_prompt", "auto")
            .appendQueryParameter("scope", scope)
            .build()

        return Intent(Intent.ACTION_VIEW, intentUri)

    }
}
