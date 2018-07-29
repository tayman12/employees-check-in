package com.virgingates.utils

import com.virgingates.verticles.MainVerticle
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import java.text.SimpleDateFormat
import java.util.*


private val LOGGER = LoggerFactory.getLogger(MainVerticle::class.java)

const val DEFAULT_TIME_FORMAT = "HH:mm:SS"
const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"
const val DEFAULT_DATE_TIME_FORMAT = "$DEFAULT_DATE_FORMAT $DEFAULT_TIME_FORMAT"

fun parseDate(dateAsString: String, format: String): Date = SimpleDateFormat(format).parse(dateAsString)

fun formatDate(date: Date, format: String): String = SimpleDateFormat(format).format(date)

fun validateJson(routingContext: RoutingContext, jsonObject: JsonObject, vararg expectedKeys: String): Boolean {
    if (!Arrays.stream(expectedKeys).allMatch { jsonObject.containsKey(it) }) {
        LOGGER.error("Bad jsonObject creation JSON payload: " + jsonObject.encodePrettily() + " from " + routingContext.request().remoteAddress())

        val response = JsonObject();

        response.put("success", false)
        response.put("error", "Bad request payload")

        routingContext.response().statusCode = 400
        routingContext.response().putHeader("Content-Type", "application/json")
        routingContext.response().end(response.encodePrettily())

        return false
    }
    return true
}

fun createEmptySuccessResponse(routingContext: RoutingContext) {
    val response = JsonObject().put("success", true)

    routingContext.response().statusCode = 200
    routingContext.response().putHeader("Content-Type", "application/json")
    routingContext.response().end(response.encodePrettily())
}
