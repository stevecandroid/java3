package com.xt.java3.base;

/**
 * Created by steve on 17-10-22.
 */

public class BaseError extends RuntimeException {
    private int status;

    public BaseError(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
