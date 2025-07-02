package com.vzkz.core.data

import com.vzkz.core.domain.log.BeeLogger
import com.vzkz.core.domain.log.LogType

object BeeLog {
    private var logger: BeeLogger = BeeLoggerImpl

    fun log(message: String, tag: String, logType: LogType) {
        logger.log(message, tag, logType)
    }
}
