package com.virgingates.wrapper

import com.virgingates.mapper.IUserToSheetMapper
import com.virgingates.utils.DEFAULT_DATE_FORMAT
import com.virgingates.utils.DEFAULT_TIME_FORMAT
import com.virgingates.utils.GoogleSheetInterface
import com.virgingates.utils.formatDate
import com.virgingates.utils.parseDate
import java.util.Date
import java.util.Arrays

class GoogleSheetWrapper(private val userToSheetMapper: IUserToSheetMapper) : SheetWrapper {

    private val googleSheetInterface = GoogleSheetInterface()
    private var spreadSheetId: String = ""

    override fun setUserId(userId: String) {
        spreadSheetId = userToSheetMapper.getSpreadSheetId(userId) ?: createSheet()
    }

    override fun createSheet(): String = googleSheetInterface.createSheet()

    override fun updateCheckInTime(checkInDate: Date, forceUpdate: Boolean) {
        val currentData = googleSheetInterface.readRange(spreadSheetId, "A1:D")

        val recordIndex = getDateRecordIndex(currentData, checkInDate)

        if (forceUpdate || recordIndex > currentData.size || currentData[recordIndex - 1][1] == "") {
            val date = formatDate(checkInDate, DEFAULT_DATE_FORMAT)
            val time = formatDate(checkInDate, DEFAULT_TIME_FORMAT)

            val values = Arrays.asList(Arrays.asList<Any>(date, time))

            googleSheetInterface.writeRange(spreadSheetId, "A$recordIndex", values)
        }
    }

    override fun updateCheckOutTime(checkOutDate: Date) {
        val currentData = googleSheetInterface.readRange(spreadSheetId, "A1:D")

        val recordIndex = getDateRecordIndex(currentData, checkOutDate)

        var values = Arrays.asList(Arrays.asList<Any>(formatDate(checkOutDate, DEFAULT_DATE_FORMAT)))
        googleSheetInterface.writeRange(spreadSheetId, "A$recordIndex", values)

        values = Arrays.asList(Arrays.asList<Any>(formatDate(checkOutDate, DEFAULT_TIME_FORMAT)))
        googleSheetInterface.writeRange(spreadSheetId, "C$recordIndex", values)
    }

    override fun getCheckInTime(checkInDate: String) = getTime(checkInDate, true)

    override fun getCheckOutTime(checkOutDate: String) = getTime(checkOutDate, false)

    private fun getTime(checkInDate: String, isCheckIn: Boolean): String {
        val currentData = googleSheetInterface.readRange(spreadSheetId, "A1:D")

        val recordIndex = getDateRecordIndex(currentData, parseDate(checkInDate, DEFAULT_DATE_FORMAT))

        return currentData[recordIndex - 1][if (isCheckIn) 1 else 2] as String
    }

    private fun getDateRecordIndex(data: List<List<Any>>, timestamp: Date): Int {
        val dateAsString = formatDate(timestamp, DEFAULT_DATE_FORMAT)

        if (!data.isEmpty()) {
            for (i in data.size downTo 1)
                if (dateAsString == data[i - 1][0])
                    return i
        }

        return data.size + 1
    }
}
