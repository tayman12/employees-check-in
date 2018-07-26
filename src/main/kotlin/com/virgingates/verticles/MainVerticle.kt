package com.virgingates.verticles

import com.virgingates.utils.createEmptySuccessResponse
import com.virgingates.utils.defaultDateTimeFormat
import com.virgingates.utils.parseDate
import com.virgingates.utils.validateJson
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler

class MainVerticle : AbstractVerticle() {

  private val LOGGER = LoggerFactory.getLogger(MainVerticle::class.java)

  override fun start(startFuture: Future<Void>) {
    val router = Router.router(vertx)

    router.post().handler(BodyHandler.create())
    router.post("/check-in").handler(this::checkIn)
    router.post("/check-out").handler(this::checkOut)

    val httpServer = vertx.createHttpServer()

    httpServer.requestHandler(router::accept).listen(8080);

    startFuture.succeeded();
  }

  private fun checkIn(routingContext: RoutingContext) {
    val checkInRequest = routingContext.bodyAsJson

    if (!validateJson(routingContext, checkInRequest, "id", "timestamp", "latitude", "longitude"))
      return

    handleValidCheckIn(checkInRequest)

    createEmptySuccessResponse(routingContext)
  }

  private fun checkOut(routingContext: RoutingContext) {
    val checkInRequest = routingContext.bodyAsJson

    if (!validateJson(routingContext, checkInRequest, "id", "timestamp", "latitude", "longitude"))
      return

    handleValidCheckIn(checkInRequest)

    createEmptySuccessResponse(routingContext)
  }


  private fun handleValidCheckIn(checkInRequest: JsonObject) {
    LOGGER.info(checkInRequest)

    val id = checkInRequest.getString("id")
    val latitude = checkInRequest.getString("latitude")
    val longitude = checkInRequest.getString("longitude")
    val dateAsString = checkInRequest.getString("timestamp");
    val timestamp = parseDate(dateAsString, defaultDateTimeFormat)

    println("$id , lat $latitude, long $longitude, time $timestamp")
  }
}

