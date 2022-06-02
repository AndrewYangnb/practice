package com.spark;
import static spark.Spark.*;

public class HelloWord {
    public static void main(String[] args) {
        get("/hello", (request, response) -> "Hello World: " + request.ip() + request.port());

        get("/private", (request, response) -> {
            response.status(401);
            return "Go away!!!";
        });

        get("/redirect", (request, response) -> {
            response.redirect("/news/world");
            return null;
        });

        get("/news/world", (request, response) -> "Welcome to the News World.");

        get("/", (request, response) -> "root");

        get("/hello/:name", (request, response) -> "Hello: " + request.params("name"));

        get("/news/:section", (request, response) -> {
            response.type("text/xml");
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><news>" + request.params("section") + "</news>";
        });

        get("/protected", (request, response) -> {
            halt(403, "I don't think so!!!");
            return null;
        });

        get("/say/*/to/*", (request,response) -> "Number of splat parameters: " + request.splat().length);

        post("/hello/world", (request, response) ->
            // create something
            "Hello World: " + request.body()
        );

//        unmap("/hello");

    }
}
