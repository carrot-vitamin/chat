package com.example.chat.controller;

import com.example.chat.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

/**
 * @author yinshaobo on 2019-09-25 14:40
 */
@Controller
public class WebSocketController {

    @Autowired
    private WebSocketServer server;

    @PostMapping("/login")
    public String login(String username,String password) throws IOException {
        //TODO: 校验密码
        server.sendInfo(username + "进入了聊天室!");
        return "index";
    }
}
