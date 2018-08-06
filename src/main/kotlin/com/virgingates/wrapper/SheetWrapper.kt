package com.virgingates.wrapper

import java.util.*

interface SheetWrapper {

    fun updateCheckInTime(checkInDate: Date, forceUpdate: Boolean)
    fun updateCheckOutTime(checkOutDate: Date)
    fun getCheckInTime(checkInDate: String): String
    fun getCheckOutTime(checkInDate: String): String
    fun createSheet(): String
    fun setUserId(userId: String)
}
