package com.dd.callmonitor.data.server.routes.root

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

internal class ResponseTimeFormatter(private val locale: Locale) {

    fun format(timeMillisUtc: Long) = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ssZZZZZ",
        locale
    ).apply { timeZone = TimeZone.getDefault() }.format(timeMillisUtc)
}
