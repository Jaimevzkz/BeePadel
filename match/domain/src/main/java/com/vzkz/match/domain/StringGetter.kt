package com.vzkz.match.domain

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.GenericError
import com.vzkz.core.domain.error.Result

interface StringGetter {
    fun getString(resId: Int): String?
}