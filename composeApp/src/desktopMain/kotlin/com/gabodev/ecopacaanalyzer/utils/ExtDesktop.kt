package com.gabodev.ecopacaanalyzer.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun Long.toFormattedDate(): String {
    val date = Date(this * 1000)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}