package com.virgingates.verticles

import com.virgingates.context.Context
import com.virgingates.mapper.UserToSheetMapper
import com.virgingates.processors.UpdateCheckInProcessor
import com.virgingates.processors.UpdateCheckOutProcessor
import com.virgingates.wrapper.GoogleSheetWrapper
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler

const val HTTP_PORT = 8080

class MainVerticle : AbstractVerticle() {

    private val userToSheetMapper = UserToSheetMapper()

    override fun start(startFuture: Future<Void>) {
        val router = Router.router(vertx)

        router.post().handler(BodyHandler.create())
        router.post("/").handler(this::healthCheck)
        router.post("/check-in").handler(this::checkIn)
        router.post("/check-out").handler(this::checkOut)

        val httpServer = vertx.createHttpServer()

        httpServer.requestHandler(router::accept).listen(HTTP_PORT)

        startFuture.succeeded()
    }

    private fun healthCheck(routingContext: RoutingContext) {
        HealthCheckProcessor(Context(routingContext), GoogleSheetWrapper(userToSheetMapper)).execute()
    }

   private fun checkIn(routingContext: RoutingContext) {
        UpdateCheckInProcessor(Context(routingContext), GoogleSheetWrapper(userToSheetMapper)).execute()
    }

    private fun checkOut(routingContext: RoutingContext) {
        UpdateCheckOutProcessor(Context(routingContext), GoogleSheetWrapper(userToSheetMapper)).execute()
    }
}
