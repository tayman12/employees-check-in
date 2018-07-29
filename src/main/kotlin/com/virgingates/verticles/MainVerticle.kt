package com.virgingates.verticles

import com.virgingates.utils.*
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import java.util.*

class MainVerticle : AbstractVerticle() {

    private val LOGGER = LoggerFactory.getLogger(MainVerticle::class.java)
    private val userIdToSheetId = Properties()

    init {
        userIdToSheetId.load(this::class.java.classLoader.getResourceAsStream("sheetsMapping.properties"))
    }

    override fun start(startFuture: Future<Void>) {
        val router = Router.router(vertx)

        router.post().handler(BodyHandler.create())
        router.post("/check-in").handler(this::checkIn)
        router.post("/check-out").handler(this::checkOut)

        router.get("/check-in").handler(this::getCheckInTime)
        router.get("/check-out").handler(this::getCheckOutTime)

        val httpServer = vertx.createHttpServer()

        httpServer.requestHandler(router::accept).listen(8080);

        startFuture.succeeded();
    }

    private fun checkIn(routingContext: RoutingContext) {
        val checkInRequest = routingContext.bodyAsJson

        if (!validateJson(routingContext, checkInRequest, "id", "timestamp"))
            return

        handleValidCheckIn(checkInRequest)

        createEmptySuccessResponse(routingContext)
    }

    private fun handleValidCheckIn(checkInRequest: JsonObject) {
        LOGGER.info(checkInRequest)

        val id = checkInRequest.getString("id")
        val forceUpdate = checkInRequest.getBoolean("forceUpdate", false)
        val checkInDate = parseDate(checkInRequest.getString("timestamp"), DEFAULT_DATE_TIME_FORMAT)

        val googleSheetsUtil = GoogleSheetsUtil()

        val spreadSheetId = getSpreadSheetId(id)

        val currentData = googleSheetsUtil.readRange(spreadSheetId, "A1:D")

        val checkInRecordIndex = getDateRecordIndex(currentData, checkInDate)

        if (forceUpdate || checkInRecordIndex > currentData.size || currentData[checkInRecordIndex - 1][1] == "") {
            val values = Arrays.asList(Arrays.asList<Any>(formatDate(checkInDate, DEFAULT_DATE_FORMAT), formatDate(checkInDate, DEFAULT_TIME_FORMAT)))

            googleSheetsUtil.writeRange(spreadSheetId, "A$checkInRecordIndex", values)
        }
    }

    private fun checkOut(routingContext: RoutingContext) {
        val checkInRequest = routingContext.bodyAsJson

        if (!validateJson(routingContext, checkInRequest, "id", "timestamp"))
            return

        handleValidCheckOut(checkInRequest)

        createEmptySuccessResponse(routingContext)
    }

    private fun handleValidCheckOut(checkOutRequest: JsonObject) {
        LOGGER.info(checkOutRequest)

        val id = checkOutRequest.getString("id")
        val checkOutDate = parseDate(checkOutRequest.getString("timestamp"), DEFAULT_DATE_TIME_FORMAT)

        val googleSheetsUtil = GoogleSheetsUtil()

        val spreadSheetId = getSpreadSheetId(id)

        val currentData = googleSheetsUtil.readRange(spreadSheetId, "A1:D")

        val checkOutRecordIndex = getDateRecordIndex(currentData, checkOutDate)

        var values = Arrays.asList(Arrays.asList<Any>(formatDate(checkOutDate, DEFAULT_DATE_FORMAT)))
        googleSheetsUtil.writeRange(spreadSheetId, "A$checkOutRecordIndex", values)

        values = Arrays.asList(Arrays.asList<Any>(formatDate(checkOutDate, DEFAULT_TIME_FORMAT)))
        googleSheetsUtil.writeRange(spreadSheetId, "C$checkOutRecordIndex", values)
    }

    private fun getCheckInTime(routingContext: RoutingContext) {
        getTime(routingContext, true)
    }

    private fun getCheckOutTime(routingContext: RoutingContext) {
        getTime(routingContext, false)
    }

    private fun getTime(routingContext: RoutingContext, isCheckIn: Boolean) {
        val id = routingContext.request().getParam("id")
        val requestDate = routingContext.request().getParam("date")

        val googleSheetsUtil = GoogleSheetsUtil()

        val spreadSheetId = getSpreadSheetId(id)

        val currentData = googleSheetsUtil.readRange(spreadSheetId, "A1:D")

        val recordIndex = getDateRecordIndex(currentData, parseDate(requestDate, DEFAULT_DATE_FORMAT))

        val response = JsonObject();

        response.put("success", true)
        response.put("time", currentData[recordIndex - 1][if (isCheckIn) 1 else 2])

        routingContext.response().statusCode = 200
        routingContext.response().putHeader("Content-Type", "application/json")
        routingContext.response().end(response.encodePrettily())
    }

    private fun getDateRecordIndex(currentData: List<List<Any>>, date: Date): Int {
        val dateAsString = formatDate(date, DEFAULT_DATE_FORMAT)

        for (i in currentData.size downTo 1) {
            if (dateAsString == currentData[i - 1][0])
                return i
        }

        return currentData.size + 1
    }

    private fun getSpreadSheetId(userId: String): String {
        return userIdToSheetId.getValue(userId) as String
    }
}

