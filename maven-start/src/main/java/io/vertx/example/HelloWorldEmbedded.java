package io.vertx.example;

import java.util.function.Consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.DeploymentOptions;
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

    vertx.createHttpServer().requestHandler(router::accept).listen(80);
  }

  private void handleQ1(RoutingContext routingContext) {
    String type = routingContext.request().getParam("type");
    String data = routingContext.request().getParam("data");
    String res = "";
    System.out.println(type);
    HttpServerResponse response = routingContext.response();
    if (type == null || data == null || data.length() == 0) {
      response.end();
    } else {
        if (type.equals("encode")) {
            QREncryption ins = new QREncryption(data);
            res = ins.generateQRCode();
            
        } else if (type.equals("decode")) {
            try {
                QRDecryption dec = new QRDecryption(data);
                dec.decode();
                dec.extractMatrix();
                dec.getMarix();
                res = dec.zigzagDecode();
            } catch (Exception e) {
                // e.printStackTrace();
                res = "";
            }
            
            // res = "CC Team is awesome!";
        }
      response.end(res);
    }
    // QREncryption ins = new QREncryption();
    // if (type.equals("encode")) {
    //     res = ins.generateQRCode(data);
    // } else if (type.equals("decode")) {
    //     res = "CC Team is awesome!";
    // }
    // response.end(res);
  }

  private void handleQ2(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    response.end();
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

  public static void runExample(String verticleID) {
        // VertxOptions options = new VertxOptions();
        DeploymentOptions options = new DeploymentOptions().setInstances(8);
       

        Consumer<Vertx> runner = vertx -> {
            vertx.deployVerticle(verticleID, new DeploymentOptions().setInstances(8));
        };
        // Vertx vertx.deployVerticle(verticleID, options);
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(verticleID, options);

        runner.accept(vertx);
    }


}

