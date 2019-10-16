package com.example.chat.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yinshaobo on 2019/10/16 11:20
 */
@Setter
@Getter
@ToString
public class User {

    private Integer id;

    private String username;

    private String password;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User u = (User) obj;
            return this.username.equals(u.getUsername()) && this.password.equals(u.getPassword());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
