package io.vertx.example;

import java.util.function.Consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;


public class HelloWorldEmbedded extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    String verticleID = HelloWorldEmbedded.class.getName();
    runExample(verticleID);
  }


  @Override
  public void start() {

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.get("/q1").handler(this::handleQ1);
    router.put("/q2").handler(this::handleQ2);

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }

  private void handleQ1(RoutingContext routingContext) {
    String type = routingContext.request().getParam("type");
    String data = routingContext.request().getParam("data");
    System.out.println(type);
    System.out.println(data);
    String res = "0x66d92b800x5bc76d830x121a7fa60x51c111870x3a5f3ca30x8be36a130xedb223a0xfc8e98780x33bf50de0x2e8709700x545a2d0f0xecef7ae0x461175cd0xff132a";
    HttpServerResponse response = routingContext.response();
    if (type == null || data == null) {
      response.end();
    } else {
      response.end(res);
    }
  }

  private void handleQ2(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    response.end();
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

  public static void runExample(String verticleID) {
        VertxOptions options = new VertxOptions();

        Consumer<Vertx> runner = vertx -> {
            vertx.deployVerticle(verticleID);
        };
        
        Vertx vertx = Vertx.vertx(options);

        runner.accept(vertx);
    }


}

