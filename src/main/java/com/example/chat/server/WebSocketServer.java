package com.example.chat.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yinshaobo on 2019-09-25 14:38
 */
@Slf4j
@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static volatile int onlineCount;

    /**
     * 创建一个线程安全的map
     */
    private static Map<String, WebSocketServer> users = Collections.synchronizedMap(new HashMap<>());

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 放入map中的key,用来表示该连接对象
     */
    private String userId;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        //加入map中,为了测试方便使用username做key
        users.put(userId, this);
        //在线数加1
        addOnlineCount();
        log.info("【{}】加入！当前在线人数为【{}】", userId, getOnlineCount());
        try {
            this.session.getBasicRemote().sendText("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常", e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //从set中删除
        users.remove(this.userId);
        //在线数减1
        subOnlineCount();
        log.info("一个连接关闭！当前在线人数为【{}】", getOnlineCount());
    }

    /**
     * 收到客户端消息后触发的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("来自客户端的消息:" + message);
        //群发消息
        try {
            if (StringUtils.isEmpty(message)) {
                return;
            }
            //如果给所有人发消息携带@ALL, 给特定人发消息携带@xxx@xxx#message
            String[] split = message.split("#");
            if (split.length > 1) {
                String[] users = split[0].split("@");
                if (users.length < 2) {
                    return;
                }
                String firstUser = users[1].trim();
                if (StringUtils.isEmpty(firstUser) || "ALL".equals(firstUser.toUpperCase())) {
                    String msg = userId + ": " + split[1];
                    //群发消息
                    sendInfo(msg);
                } else {//给特定人员发消息
                    for (String user : users) {
                        if (!StringUtils.isEmpty(user.trim())) {
                            this.sendMessageToSomeBody(user.trim(), split[1]);
                        }
                    }
                }
            } else {
                sendInfo(userId + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误 session: {}", session, error);
    }

    /**
     * 给特定人员发送消息
     *
     * @param userId 发送对象
     * @param message  消息内容
     * @throws IOException IOException
     */
    private void sendMessageToSomeBody(String userId, String message) throws IOException {
        if (users.get(userId) == null) {
            return;
        }
        users.get(userId).session.getBasicRemote().sendText(this.userId + "发来的私信：" + message);
        this.session.getBasicRemote().sendText(this.userId + "@" + userId + ": " + message);
    }

    /**
     * 群发自定义消息
     */
    public void sendInfo(String message) {
        for (WebSocketServer item : users.values()) {
            try {
                item.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
