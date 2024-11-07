package com.gabodev.ecopacaanalyzer.utils

fun String?.orEmpty(): String = this ?: ""

expect fun Long.toFormattedDate(): String