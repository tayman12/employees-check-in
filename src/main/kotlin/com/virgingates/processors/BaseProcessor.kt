package com.virgingates.processors

import com.virgingates.context.IContext
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import java.util.*

open abstract class BaseProcessor(val routingContext: IContext) {

    fun execute() {
        try {
            logger().info(body())

            validate()
            process()
        } catch (e: IllegalStateException) {
            invalidResponse(400, "Invalid request")
        } catch (e: Exception) {
            invalidResponse(500, "Internal server error")
        }
    }

    //TODO: figure out sub class name here
    abstract fun logger(): Logger

    abstract fun validate()

    abstract fun process()

    fun body() = routingContext.bodyAsJson()

    @Throws
    fun validateJson(jsonObject: JsonObject, vararg expectedKeys: String) {
        if (!Arrays.stream(expectedKeys).allMatch { jsonObject.containsKey(it) }) {
            logger().error("Bad jsonObject creation JSON payload: " + jsonObject.encodePrettily())
            throw IllegalStateException("Mandatory parameters are missing")
        }
    }

    fun createEmptySuccessResponse() {
        val response = JsonObject().put("success", true)

        routingContext.setStatusCode(200)
        routingContext.putHeader("Content-Type", "application/json")
        routingContext.setResponseBody(response)
    }

    private fun invalidResponse(statusCode: Int, message: String) {
        val response = JsonObject();

        response.put("success", false)
        response.put("error", message)

        routingContext.setStatusCode(statusCode)
        routingContext.putHeader("Content-Type", "application/json")
        routingContext.setResponseBody(response)
    }
}