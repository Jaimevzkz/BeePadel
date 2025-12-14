package com.vzkz.match.data

import android.content.Context
import com.vzkz.core.domain.error.GenericError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.StringGetter
import timber.log.Timber

class StringGetterImpl(
    private val context: Context
): StringGetter {
    override fun getString(resId: Int): String? {
        return try {
            context.getString(resId)
        } catch (e: Exception){
            Timber.e("Error parsing String Res to String: ${e.toString()}")
            null
        }
    }
}