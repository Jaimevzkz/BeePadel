package com.vzkz.beepadel.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.vzkz.beepadel.BeePadelApp
import com.vzkz.beepadel.MainViewmodel
import com.vzkz.match.presentation.active_match.ActiveMatchViewmodel
import kotlinx.coroutines.CoroutineScope
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as BeePadelApp).applicationScope
    }
    single<SharedPreferences> {
        EncryptedSharedPreferences(
            androidApplication(),
            "auth_pref",
            MasterKey(androidApplication()),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    viewModelOf(::MainViewmodel)

    single<AuthorizationService> {
        AuthorizationService(androidApplication())
    }
}