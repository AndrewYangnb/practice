package com.spark;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;




/**
 * WebSocket 处理器
 */

@WebSocket
public class ChatWebSocketHandler {

    public static final Logger logger = LogManager.getLogger(ChatWebSocketHandler.class.getName());

    // 定义两个String，用来给函数调用时传参赋值。
    private String sender, msg;


    /**
     * 获取session连接，并保存
     * @param user
     * @throws
     */
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        // userNameMap中的value存储格式为 User1，User2
        String username = "User" + Chat.nextUserNumber++;
        // 把当前用户的session 和username 保存到userUsernameMap
        Chat.userUsernameMap.put(user, username);
        // 通过调用broadcastMessage函数给其他user发送一条提示信息
        Chat.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
    }

    /**
     * 关闭session连接
     * @param user
     * @param statusCode
     * @param reason
     */
    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = Chat.userUsernameMap.get(user);
        // 删除userUsernameMap 中的user条目
        Chat.userUsernameMap.remove(user);
        // 发送提示信息
        Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
    }

    /**
     * 发送消息
     * @param user
     * @param message
     */
    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        // 发送消息
        Chat.broadcastMessage(sender = Chat.userUsernameMap.get(user), msg = message);
    }

}
