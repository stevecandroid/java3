package com.xt.java3.modules.response;

import com.xt.java3.User;

/**
 * Created by steve on 17-10-24.
 */

public class LoginResponse extends BaseResponse {

    private User user;

    public LoginResponse(int status, User user) {
        super(status);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
