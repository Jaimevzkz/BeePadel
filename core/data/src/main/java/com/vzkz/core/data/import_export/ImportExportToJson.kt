package com.vzkz.core.data.import_export

import com.vzkz.core.data.import_export.model.ExportMatchListSerializable
import com.vzkz.core.data.import_export.model.toExportMatchSerializable
import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.core.domain.ImportExportRepository
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.ImportExportError
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.flow.first
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

class ImportExportToJson(
    private val localStorageRepository: LocalStorageRepository,
    private val json: Json
) : ImportExportRepository {
    override suspend fun exportData(outputStream: OutputStream): EmptyResult<ImportExportError> {
        val exportMatchListSerializable = ExportMatchListSerializable(
            exportedAt = System.currentTimeMillis(),
            items = localStorageRepository.getMatchHistory().first()
                .map { it.toExportMatchSerializable() }
        )

        return try {
            outputStream.writer().use {
                val jsonStr = json.encodeToString(exportMatchListSerializable)
                it.write(jsonStr)
            }
            Result.Success(Unit)
        } catch (ioException: IOException) {
            Timber.e("Export match data error: ${ioException.toString()}")
            Result.Error(ImportExportError.EXPORT_FAILURE)
        }
    }

    override suspend fun importData(inputStream: InputStream): EmptyResult<ImportExportError> {
        return try {
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val exportData = json.decodeFromString<ExportMatchListSerializable>(jsonString)
            val failedImportNumber =
                localStorageRepository.insertMatchList(exportData.items.map { it.toMatch() })
            if (failedImportNumber == exportData.items.size)
                Result.Error(ImportExportError.TOTAL_IMPORT_FAILURE)
            else if (failedImportNumber > 0)
                Result.Error(ImportExportError.PARTIAL_IMPORT_FAILURE)
            else
                Result.Success(Unit)
        } catch (e: SerializationException) {
            Timber.e(e.toString())
            Result.Error(ImportExportError.BADLY_FORMED_JSON)
        } catch (e: IllegalArgumentException) {
            Timber.e(e.toString())
            Result.Error(ImportExportError.WRONG_MATCH_LIST_STRUCTURE)
        }

    }
}