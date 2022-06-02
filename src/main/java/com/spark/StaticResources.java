package com.spark;
import static spark.Spark.*;


public class StaticResources {
    public static void main(String[] args) {

        staticFileLocation("/public");

        get("/hello", (request, response) -> "Hello, World!");


    }
}
