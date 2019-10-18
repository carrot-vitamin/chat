package com.example.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.websocket.Session;

/**
 * @author yinshaobo on 2019/10/17 17:58
 */
@AllArgsConstructor
@Data
public class Client {

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 放入map中的key,用来表示该连接对象
     */
    private String username;

}
