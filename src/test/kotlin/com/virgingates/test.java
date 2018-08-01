package com.virgingates;

import com.virgingates.verticles.MainVerticle;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class test {

    @NotNull
    public Vertx vertx;
    @NotNull
    public WebClient webClient;

    @NotNull
    public final Vertx getVertx() {
        Vertx var10000 = this.vertx;
        if (this.vertx == null) {
            Intrinsics.throwUninitializedPropertyAccessException("vertx");
        }

        return var10000;
    }

    public final void setVertx(@NotNull Vertx var1) {
        Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
        this.vertx = var1;
    }

    @NotNull
    public final WebClient getWebClient() {
        WebClient var10000 = this.webClient;
        if (this.webClient == null) {
            Intrinsics.throwUninitializedPropertyAccessException("webClient");
        }

        return var10000;
    }

    public final void setWebClient(@NotNull WebClient var1) {
        Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
        this.webClient = var1;
    }

    @Before
    public final void setUp(@NotNull TestContext testContext) {
        Intrinsics.checkParameterIsNotNull(testContext, "testContext");
        Vertx var10001 = Vertx.vertx();
        Intrinsics.checkExpressionValueIsNotNull(var10001, "Vertx.vertx()");
        this.vertx = var10001;
        Vertx var10000 = this.vertx;
        if (this.vertx == null) {
            Intrinsics.throwUninitializedPropertyAccessException("vertx");
        }

        var10000.deployVerticle((Verticle) (new MainVerticle()));
        var10001 = this.vertx;
        if (this.vertx == null) {
            Intrinsics.throwUninitializedPropertyAccessException("vertx");
        }

        WebClient var2 = WebClient.create(var10001, (new WebClientOptions()).setDefaultHost("localhost").setDefaultPort(8080));
        Intrinsics.checkExpressionValueIsNotNull(var2, "WebClient.create(vertx, …   .setDefaultPort(8080))");
        this.webClient = var2;
    }

//    @After
//    public final void tearDown(@NotNull TestContext testContext) {
//        Intrinsics.checkParameterIsNotNull(testContext, "testContext");
//        Vertx var10000 = this.vertx;
//        if (this.vertx == null) {
//            Intrinsics.throwUninitializedPropertyAccessException("vertx");
//        }
//
//        var10000.close(testContext.asyncAssertSuccess());
//    }

    @Test
    public final void simpleCheckIn(final TestContext testContext) {
        Intrinsics.checkParameterIsNotNull(testContext, "testContext");
        JsonObject checkInRequest = (new JsonObject()).put("id", "123").put("timestamp", "2018-08-24 10:27:30");
        final Future checkInFuture = Future.future();

        webClient.post("/check-in")
                .as(BodyCodec.jsonObject())
                .sendJsonObject(checkInRequest, testContext.asyncAssertSuccess(ar -> {
                    System.out.println("Output received");
//                    if (ar.succeeded())
//                        testContext.async().complete();
//                    else

                    testContext.fail();
                }));


//
//        vertx.createHttpClient().getNow(8080, "localhost", "/check-in", responseHandler -> {
//
//        });

    }
}
