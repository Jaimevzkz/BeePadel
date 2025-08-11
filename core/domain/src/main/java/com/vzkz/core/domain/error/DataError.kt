package com.vzkz.core.domain.error

sealed interface DataError: RootError {
    enum class Local: DataError {
        INSERT_MATCH_FAILED,
        INSERT_SET_FAILED,
        INSERT_GAME_FAILED,
        DELETE_MATCH_FAILED,
        DELETE_SET_FAILED,
        DELETE_GAME_FAILED,
        // Datastore
        DISK_FULL,
    }

    enum class Logic: DataError{
        EMPTY_SET_LIST
    }

}