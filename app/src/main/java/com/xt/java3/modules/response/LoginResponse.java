package com.xt.java3.modules.response;

import com.xt.java3.User;

/**
 * Created by steve on 17-10-24.
 */

public class LoginResponse extends BaseResponse {

    private int userId;

    public LoginResponse(int status, int userId) {
        super(status);
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUser(int userId) {
        this.userId = userId;
    }
}
