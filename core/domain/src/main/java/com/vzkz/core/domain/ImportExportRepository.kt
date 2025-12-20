package com.vzkz.core.domain

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.ImportExportError
import java.io.OutputStream

interface ImportExportRepository {
    suspend fun exportData(outputStream: OutputStream): EmptyResult<ImportExportError>
}