package com.xt.java3.modules.response;

/**
 * Created by steve on 17-10-22.
 */

public class BaseResponse {
    private int status ;

    public BaseResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
