package com.xt.java3.modules.request;

/**
 * Created by steve on 17-10-22.
 */

public class RegisterBody  {

    private String nickname;
    private String password;

    public RegisterBody(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
