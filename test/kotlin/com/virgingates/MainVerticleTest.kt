//package com.virgingates
//
//import com.virgingates.verticles.MainVerticle
//import io.vertx.core.Vertx
//import io.vertx.core.json.JsonObject
//import io.vertx.ext.unit.TestContext
//import io.vertx.ext.unit.junit.VertxUnitRunner
//import io.vertx.ext.web.client.WebClient
//import io.vertx.ext.web.client.WebClientOptions
//import io.vertx.ext.web.codec.BodyCodec
//import org.junit.Test
//import org.junit.runner.RunWith
//
//// End To End Test - Acceptance test
//@RunWith(VertxUnitRunner::class)
//class MainVerticleTest {
//
//    lateinit var vertx: Vertx
//    lateinit var webClient: WebClient
//
//    private fun setUp() {
//        vertx = Vertx.vertx()
//
//        vertx.deployVerticle(MainVerticle())
//
//        webClient = WebClient.create(vertx, WebClientOptions()
//                .setDefaultHost("localhost")
//                .setDefaultPort(8080))
//    }
//
//    @Test
//    fun checkIn(testContext: TestContext) {
//        setUp()
//        val checkInRequest = JsonObject()
//                .put("id", "123")
//                .put("timestamp", "2018-08-24 10:27:30")
//
//        webClient.post("/check-in")
//                .`as`(BodyCodec.jsonObject())
//                .sendJsonObject(checkInRequest, testContext.asyncAssertSuccess { response ->
//                    println("Response Received")
//                    if (response.statusCode() != 200)
//                        testContext.fail()
//                    vertx.close(testContext.asyncAssertSuccess())
//                })
//    }
//
//    @Test
//    fun checkOut(testContext: TestContext) {
//        setUp()
//        val checkInRequest = JsonObject()
//                .put("id", "123")
//                .put("timestamp", "2018-08-24 10:27:30")
//
//        webClient.post("/check-out")
//                .`as`(BodyCodec.jsonObject())
//                .sendJsonObject(checkInRequest, testContext.asyncAssertSuccess { response ->
//                    println("Response Received")
//                    if (response.statusCode() != 200)
//                        testContext.fail()
//                    vertx.close(testContext.asyncAssertSuccess())
//                })
//    }
//}
