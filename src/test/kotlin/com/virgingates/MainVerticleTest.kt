package com.virgingates

import com.virgingates.verticles.MainVerticle
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.ext.web.codec.BodyCodec
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import io.vertx.groovy.ext.unit.Completion_GroovyExtension.handler


//Integration Test
@RunWith(VertxUnitRunner::class)
class MainVerticleTest {

    lateinit var vertx: Vertx
    lateinit var webClient: WebClient

    @Before
    fun setUp(testContext: TestContext) {
        vertx = Vertx.vertx()

        vertx.deployVerticle(MainVerticle())

        webClient = WebClient.create(vertx, WebClientOptions()
                .setDefaultHost("localhost")
                .setDefaultPort(8080))
    }

    @After
    fun tearDown(testContext: TestContext) {
        testContext.async().complete()
        println("after method")
        vertx.close(testContext.asyncAssertSuccess())
    }

    @Test
    fun checkIn(testContext: TestContext) {
        val checkInRequest = JsonObject()
                .put("id", "123")
                .put("timestamp", "2018-08-24 10:27:30")

        webClient.post("/check-in")
                .`as`(BodyCodec.jsonObject())
                .sendJsonObject(checkInRequest, testContext.asyncAssertSuccess { response ->
                    println("Response Received")
                    if (response.statusCode() != 200)
                        testContext.fail()
                })
    }

    @Test
    fun checkOut(testContext: TestContext) {
        val checkInRequest = JsonObject()
                .put("id", "123")
                .put("timestamp", "2018-08-24 10:27:30")

        webClient.post("/check-out")
                .`as`(BodyCodec.jsonObject())
                .sendJsonObject(checkInRequest, testContext.asyncAssertSuccess { response ->
                    println("Response Received")
                    if (response.statusCode() != 200)
                        testContext.fail()
                })
    }
}
