package com.example.chat.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName: Response
 * @Description:
 * @author: yinshaobo
 * @date: 2019/3/13 2:32 PM
 */
@Setter
@Getter
@ToString
public class Response {

    private static final String FAILURE_CODE = "999999";

    private static final String SUCCESS_CODE = "000000";

    private Response() {}

    private Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private Response(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private String code;

    private String message;

    private Object data;

    public static Response success() {
        return new Response(SUCCESS_CODE, "请求成功");
    }

    public static Response success(Object data) {
        return new Response(SUCCESS_CODE, "请求成功", data);
    }

    public static Response fail() {
        return new Response(FAILURE_CODE, "请求失败");
    }

    public static Response fail(String message) {
        return new Response(FAILURE_CODE, message);
    }

}
