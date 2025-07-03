package com.vzkz.core.domain.error

sealed interface DataError: RootError {
    enum class Local: DataError {
        INSERT_MATCH_FAILED,
        DELETE_MATCH_FAILED
    }

}