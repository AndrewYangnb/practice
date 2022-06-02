package com.spark;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Redirect;


import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Example showing a very simple (and stupid) authentication filter that is
 * executed before all other resources.
 *
 * When requesting the resource with e.g.
 *     <a href="http://localhost:4567/hello?user=some&password=guy">http://localhost:4567/hello?user=some&password=guy</a>
 * the filter will stop the execution and the client will get a 401 UNAUTHORIZED with the content 'You are not welcome here'
 *
 * When requesting the resource with e.g.
 *     <a href="http://localhost:4567/hello?user=foo&password=bar">http://localhost:4567/hello?user=foo&password=bar</a>
 * the filter will accept the request and the request will continue to the /hello route.
 *
 * Note: There is a second "before filter" that adds a header to the response
 * Note: There is also an "after filter" that adds a header to the response
 */
public class FilterExample {

    private static final Logger logger = LogManager.getLogger(FilterExample.class.getName());

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FilterExample() {
        port(10000);
        logger.info("hello a!");
    }

    /**
     *
     */
    public void Filter() {
        Map<String, String> usernamePasswords = new HashMap<>();

        // 1. 获取

        usernamePasswords.put("foo", "bar");
        /*usernamePasswords.put("admin", "admin");*/

        before("/*", (request, response) -> logger.info("Received api call"));

        post("/login", (request, response) -> {
            String user = request.queryParams("user");
            String password = request.queryParams("password");
            if (!(usernamePasswords.containsKey(user))) {
                if (user == null || password == null) {
                    return "Please notice your input!!!";
                } else {
                    usernamePasswords.put(user,password);
                    return "Welcome to join us, Now please log in";
                }
            } else {
                return gson.toJson(user) + " is already created.";
            }
        });

        before("/hello",(request, response) -> {
            String user = request.queryParams("user");
            String password = request.queryParams("password");

            String dbPassword = usernamePasswords.get(user);
            if (!(password != null && password.equals(dbPassword))) {
                halt(401, "Please create a count first!!!");
                redirect.any("/hello", "/login", Redirect.Status.MOVED_PERMANENTLY);
            }
        });

        before("/hello", (request, response) -> response.header("Foo", "Set by second before filter"));

        get("/hello", (request, response) -> "Hello World!");

        after("/hello", (request, response) -> response.header("spark", "added by after-filter"));

        afterAfter("/hello", (request, response) -> response.header("finally", "executed even if exception is throw"));

        afterAfter((request, response) -> response.header("finally", "executed after any route even if exception is throw"));
    }


    public static void main(String[] args) {
        new FilterExample().Filter();
    }

}
