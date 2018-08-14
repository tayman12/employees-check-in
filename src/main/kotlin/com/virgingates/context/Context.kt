package com.virgingates.context

import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

class Context(val routingContext: RoutingContext) : IContext {

    override fun bodyAsJson() = routingContext.bodyAsJson

    override fun setStatusCode(statusCode: Int) {
        routingContext.response().statusCode = statusCode
    }

    override fun putHeader(key: String, value: String) {
        routingContext.response().putHeader(key, value)
    }

    override fun setResponseBody(response: JsonObject) {
        routingContext.response().end(response.encodePrettily())
    }

    override fun getRequestParameter(paramName: String) = routingContext.request().getParam(paramName)


}
