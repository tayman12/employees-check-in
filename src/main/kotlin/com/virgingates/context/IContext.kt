package com.virgingates.context

import io.vertx.codegen.annotations.Nullable
import io.vertx.core.json.JsonObject

interface IContext {
    fun bodyAsJson(): JsonObject
    fun setStatusCode(statusCode: Int)
    fun putHeader(key: String, value: String)
    fun setResponseBody(response: JsonObject)
    fun getRequestParameter(paramName: String): String
}