package com.virgingates

import com.virgingates.processors.UpdateCheckInProcessor
import com.virgingates.utils.DEFAULT_DATE_TIME_FORMAT
import com.virgingates.utils.parseDate
import com.virgingates.verticles.TestRoutingContextContext
import com.virgingates.wrapper.SheetWrapper
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateCheckInProcessorTest {

    private val timestamp = "2018-08-01 10:30:00"

    lateinit var sheetMapper: SheetWrapper
    lateinit var context: TestRoutingContextContext
    lateinit var updateCheckInProcessor: UpdateCheckInProcessor

    @Before
    fun setUp() {
        sheetMapper = Mockito.mock(SheetWrapper::class.java)
        context = TestRoutingContextContext()
        updateCheckInProcessor = UpdateCheckInProcessor(context, sheetMapper)
    }

    @Test
    fun testForceUpdateSkipped() {
        context.body.put("id", "123")
        context.body.put("timestamp", timestamp)

        updateCheckInProcessor.execute()

        Mockito.verify(sheetMapper).setUserId("123")
        Mockito.verify(sheetMapper).updateCheckInTime(parseDate(timestamp, DEFAULT_DATE_TIME_FORMAT), false)

        Assert.assertEquals(200, context.getStatusCode())
    }

    @Test
    fun testDoesNotForceUpdate() {
        context.body.put("id", "123")
        context.body.put("forceUpdate", false)
        context.body.put("timestamp", timestamp)

        updateCheckInProcessor.execute()

        Mockito.verify(sheetMapper).setUserId("123")
        Mockito.verify(sheetMapper).updateCheckInTime(parseDate(timestamp, DEFAULT_DATE_TIME_FORMAT), false)

        Assert.assertEquals(200, context.getStatusCode())
    }

    @Test
    fun testDoesNotForceUpdateCheckIn() {
        context.body.put("id", "123")
        context.body.put("forceUpdate", true)
        context.body.put("timestamp", timestamp)

        updateCheckInProcessor.execute()

        Mockito.verify(sheetMapper).setUserId("123")
        Mockito.verify(sheetMapper).updateCheckInTime(parseDate(timestamp, DEFAULT_DATE_TIME_FORMAT), true)

        Assert.assertEquals(200, context.getStatusCode())
    }

    @Test
    fun testMissingId() {
        context.body.put("timestamp", timestamp)

        updateCheckInProcessor.execute()

        Assert.assertEquals(400, context.getStatusCode())
    }

    @Test
    fun testMissingTimeStamp() {
        context.body.put("id", "123")

        updateCheckInProcessor.execute()

        Assert.assertEquals(400, context.getStatusCode())
    }

    @Test
    fun testInvalidDateFormat() {
        context.body.put("id", "123")
        context.body.put("timestamp", "2018/08/01 10:30:00")

        updateCheckInProcessor.execute()

        Assert.assertEquals(500, context.getStatusCode())
    }
}