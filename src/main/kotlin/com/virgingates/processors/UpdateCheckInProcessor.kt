package com.virgingates.processors

import com.virgingates.context.IContext
import com.virgingates.utils.DEFAULT_DATE_TIME_FORMAT
import com.virgingates.utils.parseDate
import com.virgingates.wrapper.SheetWrapper
import io.vertx.core.logging.LoggerFactory

class UpdateCheckInProcessor(private val context: IContext, private val googleSheetWrapper: SheetWrapper)
    : BaseProcessor(context) {

    override fun logger() = LoggerFactory.getLogger(UpdateCheckInProcessor::class.java)

    override fun validate() {
        validateJson(context.bodyAsJson(), "id", "timestamp")
    }

    // update date pattern to use utc
    override fun process() {
        val userId = body().getString("id")
        val forceUpdate = body().getBoolean("forceUpdate", false)
        val checkInDate = parseDate(body().getString("timestamp"), DEFAULT_DATE_TIME_FORMAT)

        googleSheetWrapper.setUserId(userId)
        googleSheetWrapper.updateCheckInTime(checkInDate, forceUpdate)

        createEmptySuccessResponse()
    }
}
