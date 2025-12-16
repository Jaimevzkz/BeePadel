package com.vzkz.settings.data

import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.settings.domain.SettingsRepository

class SettingsRepositoryImpl(
    private val localStorageRepository: LocalStorageRepository
): SettingsRepository {
    fun exportData(){
        /*todo:
        *  create serializable import/export DTO
        *  export to json*/

    }
}