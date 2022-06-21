package com.spark;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.eclipse.jetty.websocket.api.Session;
import static j2html.TagCreator.*;
import static spark.Spark.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多人在线聊天App
 */

public class Chat {
    // 此map用来存放 用户的 Session 和 username
    static Map<Session,String> userUsernameMap = new ConcurrentHashMap<>();

    private static final Logger logger = LogManager.getLogger(Chat.class.getName());

    // 在后面给User命名的区分
    static int nextUserNumber = 1;

    public static void main(String[] args) {
        // 加载静态文件index.html到localhost:4567
        staticFileLocation("/public");
        logger.info("webSocketHandler is staring");
        // 将给定路径映射到给定WebSocket管理程序
        webSocket("/chat", ChatWebSocketHandler.class);
        // 初始化
        init();
        //
        logger.info("webSocketHandler is working ");
    }

    /*
     * 一个用户给其他的所有用户发送消息，带上username（userUsernameMap中的values）
     */
    public static void broadcastMessage(String sender, String message) {

        // 调试代码用
        logger.info("Method broadcastMessage is deployed");
        logger.info(sender, message);
        logger.info(userUsernameMap.values());

        // 遍历userUserNameMap 中的Session，判断其是否处于连接状态
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                // sendString()方法，给其他用户发送消息，JSON格式，包括username：message和userlist两个条目
                session.getRemote().sendString(String.valueOf(new JSONObject()

                        // 这里改正错误！！！
                        .put("userMessage", createHtmlMessageFromSender(sender, message))
                        .put("userlist", userUsernameMap.values())
                ));

                // 捕获异常
            } catch (Exception e) {

                // 打印到标准错误流 （standard err stream）
                e.printStackTrace();
            }
        });
    }

    /*
    为sender（发送消息方）创建Html格式的消息，该方法被 broadcastMessage 调用
     */
    public static String createHtmlMessageFromSender(String sender, String message) {
        logger.info("Method createHtmlMessageFromSender is deployed");
        return article().with(
                // 设定此消息的格式
                b(sender + " says:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }
//    private static String createHtmlMessageFromSender(String sender, String message) {
//        return article(
//                b(sender + " says:"),
//                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
//                p(message)
//        ).render();
//    }

}

