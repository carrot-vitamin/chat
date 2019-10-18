package com.example.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.IOUtils;
import com.example.chat.model.Response;
import com.example.chat.model.User;
import com.example.chat.server.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * @author yinshaobo on 2019-09-25 14:40
 */
@Slf4j
@RestController
public class WebSocketController {

    @Autowired
    private WebSocketServer server;

    private List<User> users;

    @PostConstruct
    public void init() {
        InputStream inputStream = null;
        Scanner scanner = null;
        try {
            inputStream = WebSocketController.class.getClassLoader().getResourceAsStream("user.json");
            if (inputStream != null) {
                scanner = new Scanner(inputStream, "UTF-8");
                StringBuilder builder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    builder.append(scanner.nextLine()).append("\n");
                }
                this.users = JSONObject.parseObject(builder.toString(), new TypeReference<List<User>>() {});
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.close(scanner);
            IOUtils.close(inputStream);
        }
    }

    @PostMapping("/login")
    public Response login(User user) {
        int i = this.users.indexOf(user);
        if (i == -1) {
            return Response.fail("用户名或密码错误");
        } else {
            user = this.users.get(i);
            this.server.sendInfo(user.getUsername() + "进入了聊天室!");
            return Response.success(user);
        }
    }
}
