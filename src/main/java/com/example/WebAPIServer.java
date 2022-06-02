package com.example;

import static spark.Spark.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.collect.*;


public class WebAPIServer {

    //创建logger对象，用以记录web server日志
    private static final Logger logger = LogManager.getLogger(WebAPIServer.class.getName());

    //创建gson对象，通过toJson方法返回json格式的输出
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //Set Port Number
    public WebAPIServer() {
        port(10000);
        //服务已开启标识
        logger.info("Service Started");
    }

    public void buildRoutes() {

//每次get/post操作前更新logger
        before("/*", (q, a) -> logger.info("Received api call"));

//http://127.0.0.1:10000/hello，路由为“hello” 时返回HelloWorld
        get("/hello", (q,a) -> "Hello World");

//http://127.0.0.1:10000/:id?query=111
        get("/user/:id", (q, a) -> {

            String id = q.params("id");
            String query = q.queryParams("query");

            a.type("application/json");
//将结果转换为Json格式输出
            return gson.toJson(ImmutableMap.of("id", id, "query", query));

        });
    }

    public static void main(String[] args) {
        new WebAPIServer().buildRoutes();
    }
}


