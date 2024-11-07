package com.gabodev.ecopacaanalyzer.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeIntervalSince1970

actual fun Long.toFormattedDate(): String {
    val date = NSDate.dateWithTimeIntervalSince1970(this * 1000.0)
    val formatter = NSDateFormatter().apply {
        dateFormat = "EEE, dd 'of' MMM, yyyy"
        locale = NSLocale.currentLocale
    }
    return formatter.stringFromDate(date)
}