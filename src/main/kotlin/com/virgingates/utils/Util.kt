package com.virgingates.utils

import com.virgingates.verticles.MainVerticle
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private val LOGGER = LoggerFactory.getLogger(MainVerticle::class.java)

val defaultDateTimeFormat = "yyyy-MM-dd HH:mm:ss"

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

fun parseDate(dateAsString: String, format: String) = LocalDateTime.parse(dateAsString, DateTimeFormatter.ofPattern(format, Locale.ENGLISH))

