package com.vzkz.core.domain.error

enum class ImportExportError: RootError {
    EXPORT_FAILURE,
    TOTAL_IMPORT_FAILURE,
    PARTIAL_IMPORT_FAILURE,
    BADLY_FORMED_JSON,
    WRONG_MATCH_LIST_STRUCTURE
}