package com.example.chat.server;

import com.example.chat.model.Client;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yinshaobo on 2019-09-25 14:38
 */
@Slf4j
@ServerEndpoint("/websocket/{username}")
@Component
public class WebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static volatile int onlineCount;

    /**
     * 创建一个线程安全的map
     */
    private static Map<String, Client> users = Collections.synchronizedMap(new HashMap<>());

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        //加入map中,为了测试方便使用username做key
        users.put(session.getId(), new Client(session, username));
        //在线数加1
        addOnlineCount();
        log.info("【{}】加入！当前在线人数为【{}】", username, getOnlineCount());
        session.getAsyncRemote().sendText(this.now() + "【" + username + "】" + "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        //从set中删除
        String username = users.get(session.getId()).getUsername();
        users.remove(session.getId());
        //在线数减1
        subOnlineCount();
        log.info("一个连接【{}】关闭！当前在线人数为【{}】", username, getOnlineCount());
        if (!users.isEmpty()) {
            this.sendInfo(username + "退出了房间");
        }
        try {
            session.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 收到客户端消息后触发的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("来自客户端的消息:" + message);

        String username = users.get(session.getId()).getUsername();

        if ("ping".equalsIgnoreCase(message)) {
            log.info("接收到客户端心跳检测消息：{}", message);
            session.getAsyncRemote().sendText("HeartCheck");
            return;
        }

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
                if (StringUtils.isEmpty(firstUser) || "ALL".equalsIgnoreCase(firstUser.toUpperCase())) {
                    String msg = username + ": " + split[1];
                    //群发消息
                    sendInfo(msg);
                } else {//给特定人员发消息
                    for (String user : users) {
                        if (!StringUtils.isEmpty(user.trim())) {
                            this.sendMessageToSomeBody(session, user.trim(), split[1]);
                        }
                    }
                }
            } else {
                sendInfo(username + ": " + message);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误 session: {}", session, error);
    }

    /**
     * 给特定人员发送消息
     *
     * @param username 发送对象
     * @param message  消息内容
     * @throws IOException IOException
     */
    private void sendMessageToSomeBody(Session session, String username, String message) throws IOException {
        Client client = null;
        for (Client client1 : users.values()) {
            if (username.equalsIgnoreCase(client1.getUsername())) {
                client = client1;
                break;
            }
        }

        if (client == null) {
            log.warn("要私信的人【{}】不在聊天室！", username);
            return;
        }

        String from = users.get(session.getId()).getUsername();

        client.getSession().getAsyncRemote().sendText(this.now() + from + "发来的私信：" + message);
        session.getAsyncRemote().sendText(this.now() + from + "@" + username + ": " + message);
    }

    /**
     * 群发自定义消息
     */
    public void sendInfo(String message) {
        for (Client client : users.values()) {
            client.getSession().getAsyncRemote().sendText(this.now() + message);
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

    private String now() {
        return "【" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "】";
    }
}
