package com.vzkz.core.domain.error

sealed interface DataError: RootError {
    enum class Local: DataError {
        DELETE_MATCH_FAILED
    }

}