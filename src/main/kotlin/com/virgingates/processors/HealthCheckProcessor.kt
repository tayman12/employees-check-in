package com.virgingates.processors

import com.virgingates.context.IContext
import com.virgingates.wrapper.SheetWrapper
import io.vertx.core.logging.LoggerFactory

class HealthCheckProcessor(private val context: IContext, private val googleSheetWrapper: SheetWrapper)
    : BaseProcessor(context) {

    override fun logger() = LoggerFactory.getLogger(HealthCheckProcessor::class.java)

    override fun validate() {
    }

    override fun process() {
        createEmptySuccessResponse()
    }
}
