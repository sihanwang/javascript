package com.tr.javascript.ajax.ajaxbackend;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class HttpVerticle extends AbstractVerticle {
	
	private HashMap<Integer, JsonObject> orderlist=new HashMap<Integer, JsonObject>();

	@Override
	public void start() {
		
		Router restApiRouter = Router.router(vertx);
		restApiRouter.route().handler(BodyHandler.create());
		restApiRouter.get("/api/orders").handler(this::getOrders);
		restApiRouter.post("/api/orders").handler(this::postOrders);
		vertx.createHttpServer().requestHandler(restApiRouter::accept).listen(8080,handler -> System.out.println("Server listening on port 8080"));

	}
	
	private void getOrders(RoutingContext rc)
	{
		JsonArray array=new JsonArray();
		
		for (JsonObject order : orderlist.values())
		{
			array.add(order);
		}
		
		/*
		JsonObject json1 = new JsonObject()
				.put("id", 1)
				.put("name", "James")
				.put("drink", "Coffee");
		JsonObject json2 = new JsonObject()
				.put("id", 2)
				.put("name", "John")
				.put("drink", "Latte");
		*/
		
		rc.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .putHeader("Access-Control-Allow-Origin", "*")
        .end(array.encode());
	}
	
	private void postOrders(RoutingContext rc)
	{
		
		JsonObject newOrder=rc.getBodyAsJson();
		
		orderlist.put(newOrder.getInteger("id"), newOrder);
		System.out.println(newOrder.encode());
		
		rc.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .putHeader("Access-Control-Allow-Origin", "*")
        .end(newOrder.encode());
		
	}

}
