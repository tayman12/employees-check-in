package com.virgingates.verticles

import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.ext.web.codec.BodyCodec
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(VertxUnitRunner::class)
class APITest {

    val vertx: Vertx
    val webClient: WebClient

    init {
        vertx = Vertx.vertx()

        vertx.deployVerticle(MainVerticle())

        webClient = WebClient.create(vertx, WebClientOptions()
                .setDefaultHost("localhost")
                .setDefaultPort(8080))
    }

    @Test
    fun simpleCheckIn(testContext: TestContext) {
        val checkInRequest = JsonObject()
                .put("id", "123")
                .put("timestamp", "2018-08-24 10:27:30")

        val checkInFuture = Future.future<JsonObject>()

        webClient.post("/check-in")
                .`as`(BodyCodec.jsonObject())
                .sendJsonObject(checkInRequest) { response ->
                    print("response received")
                    if (response.succeeded()) {
                        checkInFuture.complete(response.result().body())
                    } else {
                        testContext.fail(response.cause())
                    }
                }

        val getDateRequest = JsonObject()
                .put("id", "123")
                .put("date", "2018-08-24")

        val getDateFuture = Future.future<JsonObject>()

        checkInFuture.compose({ h ->
            webClient.get("/check-in")
                    .`as`(BodyCodec.jsonObject())
                    .sendJsonObject(getDateRequest) { ar ->
                        if (ar.succeeded()) {
                            println("Here")
                            val response = ar.result().body()
                            testContext.assertEquals("10:27:30", response.getString("time"))
                            getDateFuture.complete(response)
                        } else {
                            testContext.fail(ar.cause())
                        }
                    }
        }, getDateFuture)

        getDateFuture.compose({ response ->
            testContext.assertTrue(response.getBoolean("success")!!)
            testContext.async().complete()
        }, Future.failedFuture<Any>("Oh?"))
    }
}
