package com.vzkz.core.domain.error

import com.vzkz.core.domain.error.RootError

enum class MessagingError: RootError {
    CONNECTION_INTERRUPTED,
    DISCONNECTED,
    UNKNOWN
}