package com.vzkz.core.data.import_export

import com.vzkz.core.data.import_export.model.ExportMatchListSerializable
import com.vzkz.core.data.import_export.model.toExportMatchSerializable
import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.core.domain.ImportExportRepository
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.ImportExportError
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.flow.first
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.OutputStream
import kotlin.math.exp

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
        Timber.tag("IN-APP").i("Data to export: $exportMatchListSerializable")

        return try {
            outputStream.writer().use {
                val jsonStr = json.encodeToString(exportMatchListSerializable)
                it.write(jsonStr)
                Timber.tag("IN-APP").i("string written: $jsonStr")
            }
            Result.Success(Unit)
        } catch (ioException: IOException) {
            Timber.tag("IN-APP").i("Export error: ${ioException.toString()}")
            Timber.e("Export match data error: ${ioException.toString()}")
            Result.Error(ImportExportError.EXPORT_FAILURE)
        }
    }
}