package com.virgingates.verticles

import com.virgingates.context.IContext
import io.vertx.core.json.JsonObject

// fake implementation
class TestRoutingContextContext : IContext {

    private var statusCode: Int = 0
    var body = JsonObject()
    var headers = HashMap<String, String>()
    var response = JsonObject()
    var requestParameters = HashMap<String, String>()

    override fun bodyAsJson() = body

    override fun setStatusCode(statusCode: Int) {
        this.statusCode = statusCode
    }

    override fun putHeader(key: String, value: String) {
        headers[key] = value
    }

    override fun setResponseBody(response: JsonObject) {
        this.response = response
    }

    override fun getRequestParameter(paramName: String): String = requestParameters[paramName] as String

    fun getStatusCode() = statusCode
}
