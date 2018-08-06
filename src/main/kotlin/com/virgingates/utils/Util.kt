package com.virgingates.utils

import java.text.SimpleDateFormat
import java.util.*

const val DEFAULT_TIME_FORMAT = "HH:mm:SS"
const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"
const val DEFAULT_DATE_TIME_FORMAT = "$DEFAULT_DATE_FORMAT $DEFAULT_TIME_FORMAT"

fun parseDate(dateAsString: String, format: String): Date = SimpleDateFormat(format).parse(dateAsString)

fun formatDate(date: Date, format: String): String = SimpleDateFormat(format).format(date)
