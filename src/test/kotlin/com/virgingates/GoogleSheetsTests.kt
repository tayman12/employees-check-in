package com.virgingates

import com.virgingates.mapper.UserToSheetMapper
import com.virgingates.utils.DEFAULT_DATE_FORMAT
import com.virgingates.utils.formatDate
import com.virgingates.wrapper.GoogleSheetWrapper
import com.virgingates.wrapper.SheetWrapper
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Calendar

// Integration test
class GoogleSheetsTests {

    private var sheetWrapper: SheetWrapper? = null

    @Before
    fun setUp() {
        sheetWrapper = GoogleSheetWrapper(UserToSheetMapper())
    }

    @Test
    fun testCreateNewSheet() {
        sheetWrapper!!.setUserId("unknown@virgingates.com")

        val calendar = createCalendar()

        sheetWrapper!!.updateCheckInTime(calendar.time, true)

        val checkInTime = sheetWrapper!!.getCheckInTime(formatDate(calendar.time, DEFAULT_DATE_FORMAT))

        Assert.assertEquals("10:30:00", checkInTime)
    }

    @Test
    fun testUpdateCheckIn() {
        sheetWrapper!!.setUserId("test@virgingates.com")
        val calendar = createCalendar()

        sheetWrapper!!.updateCheckInTime(calendar.time, true)

        val checkInTime = sheetWrapper!!.getCheckInTime(formatDate(calendar.time, DEFAULT_DATE_FORMAT))

        Assert.assertEquals("10:30:00", checkInTime)
    }

    @Test
    fun testUpdateCheckInForceUpdate() {
        sheetWrapper!!.setUserId("test@virgingates.com")
        val calendar = createCalendar()

        sheetWrapper!!.updateCheckInTime(calendar.time, true)

        calendar.set(Calendar.HOUR_OF_DAY, 11)

        sheetWrapper!!.updateCheckInTime(calendar.time, true)

        val checkInTime = sheetWrapper!!.getCheckInTime(formatDate(calendar.time, DEFAULT_DATE_FORMAT))

        Assert.assertEquals("11:30:00", checkInTime)
    }

    @Test
    fun testUpdateCheckInDoesNotForceUpdate() {
        sheetWrapper!!.setUserId("test@virgingates.com")
        val calendar = createCalendar()

        sheetWrapper!!.updateCheckInTime(calendar.time, true)

        calendar.set(Calendar.HOUR_OF_DAY, 11)

        sheetWrapper!!.updateCheckInTime(calendar.time, false)

        val checkInTime = sheetWrapper!!.getCheckInTime(formatDate(calendar.time, DEFAULT_DATE_FORMAT))

        Assert.assertEquals("10:30:00", checkInTime)
    }

    @Test
    fun testUpdateCheckOut() {
        sheetWrapper!!.setUserId("test@virgingates.com")
        val calendar = createCalendar()

        sheetWrapper!!.updateCheckOutTime(calendar.time)

        val checkOutTime = sheetWrapper!!.getCheckOutTime(formatDate(calendar.time, DEFAULT_DATE_FORMAT))

        Assert.assertEquals("10:30:00", checkOutTime)
    }

    private fun createCalendar(): Calendar {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, 2018)
        calendar.set(Calendar.MONTH, Calendar.AUGUST)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 10)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar
    }
}