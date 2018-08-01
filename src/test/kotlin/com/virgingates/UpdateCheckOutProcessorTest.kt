package com.virgingates

import com.virgingates.processors.UpdateCheckOutProcessor
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
class UpdateCheckOutProcessorTest {

    private val timestamp = "2018-08-01 10:30:00"

    lateinit var sheetWrapper: SheetWrapper
    lateinit var context: TestRoutingContextContext
    lateinit var updateCheckOutProcessor: UpdateCheckOutProcessor

    @Before
    fun setUp() {
        sheetWrapper = Mockito.mock(SheetWrapper::class.java)
        context = TestRoutingContextContext()
        updateCheckOutProcessor = UpdateCheckOutProcessor(context, sheetWrapper)
    }

    @Test
    fun testHappyScenario() {
        context.body.put("id", "123")
        context.body.put("timestamp", timestamp)

        updateCheckOutProcessor.execute()

        Mockito.verify(sheetWrapper).setUserId("123")
        Mockito.verify(sheetWrapper).updateCheckOutTime(parseDate(timestamp, DEFAULT_DATE_TIME_FORMAT))

        Assert.assertEquals(200, context.getStatusCode())
    }

    @Test
    fun testMissingId() {
        context.body.put("timestamp", timestamp)

        updateCheckOutProcessor.execute()

        Assert.assertEquals(400, context.getStatusCode())
    }

    @Test
    fun testMissingTimeStamp() {
        context.body.put("id", "123")

        updateCheckOutProcessor.execute()

        Assert.assertEquals(400, context.getStatusCode())
    }

    @Test
    fun testInvalidDateFormat() {
        context.body.put("id", "123")
        context.body.put("timestamp", "2018/08/01 10:30:00")

        updateCheckOutProcessor.execute()

        Assert.assertEquals(500, context.getStatusCode())
    }
}