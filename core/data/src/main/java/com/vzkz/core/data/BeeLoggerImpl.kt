package com.vzkz.core.data

import android.util.Log
import com.vzkz.core.domain.log.BeeLogger
import com.vzkz.core.domain.log.LogType

class BeeLoggerImpl: BeeLogger {
    override fun log(message: String, tag: String, logType: LogType) {
        when(logType){
            LogType.INFO -> Log.i(tag, message)
            LogType.ERROR -> Log.e(tag, message)
            LogType.WARNING -> Log.w(tag, message)
        }
    }
}