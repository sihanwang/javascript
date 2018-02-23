package com.tr.javascript.ajax.ajaxbackend;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class HttpVerticle extends AbstractVerticle {
	
	private HashMap<Integer, JsonObject> orderlist=new HashMap<Integer, JsonObject>();
	private final AtomicInteger index = new AtomicInteger(0);

	@Override
	public void start() {
		
		Router restApiRouter = Router.router(vertx);
		restApiRouter.route().handler(BodyHandler.create());
		restApiRouter.get("/api/orders").handler(this::getOrders);
		restApiRouter.post("/api/orders").handler(this::postOrders);
		restApiRouter.delete("/api/orders/:OrderID").handler(this::deleteOrder);
		restApiRouter.options("/api/orders/:OrderID").handler(this::options);
		restApiRouter.put("/api/orders/:OrderID").handler(this::putOrder);
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
		Integer thisIndex=index.getAndIncrement();
		
		JsonObject newOrder=rc.getBodyAsJson();
		newOrder.put("id", thisIndex);
		
		orderlist.put(thisIndex, newOrder);
		System.out.println(newOrder.encode());
		
		rc.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .putHeader("Access-Control-Allow-Origin", "*")
        .end(newOrder.encode());
		
	}
	
	
	private void options(RoutingContext rc)
	{
		rc.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, "text/html")
        .putHeader("Access-Control-Allow-Origin", "*")
        .putHeader("Access-Control-Allow-Headers", "appkey,X_forwarded-for,Content-Type")
        .putHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,PUT,DELETE")
        .end();
		
	}
	
	private void deleteOrder(RoutingContext rc)
	{
		Integer OrderID = Integer.parseInt( rc.request().getParam("OrderID"));
		JsonObject removedOrder=orderlist.remove(OrderID);
		
		if (removedOrder != null)
		{
			rc.response()
	        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
	        .putHeader("Access-Control-Allow-Origin", "*")
	        .end(removedOrder.encode());
		}
		else
		{
			rc.response()
	        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
	        .putHeader("Access-Control-Allow-Origin", "*")
	        .end();
		}
		
	}
	
	private void putOrder(RoutingContext rc)
	{
		Integer OrderID = Integer.parseInt( rc.request().getParam("OrderID"));
		
		JsonObject updatedOrder=rc.getBodyAsJson();
		updatedOrder.put("id", OrderID);
		
		orderlist.put(OrderID, updatedOrder);
		System.out.println(updatedOrder.encode());
		
		rc.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .putHeader("Access-Control-Allow-Origin", "*")
        .end(updatedOrder.encode());
		
	}

}
