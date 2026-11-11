package com.gabodev.iotdevicemonitor.utils

fun String?.orEmpty(): String = this ?: ""

expect fun Long.toFormattedDate(): String
/** Current time in epoch seconds (readings use epoch-second keys). */
expect fun currentEpochSeconds(): Long
