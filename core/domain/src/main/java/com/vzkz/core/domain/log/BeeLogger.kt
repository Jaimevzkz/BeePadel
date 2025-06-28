package com.vzkz.core.domain.log

interface BeeLogger {
    fun log(message: String, tag: String = "BeeLog", logType: LogType = LogType.INFO)
}