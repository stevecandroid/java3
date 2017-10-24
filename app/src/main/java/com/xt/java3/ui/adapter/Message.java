package com.xt.java3.ui.adapter;

/**
 * Created by steve on 17-10-24.
 */

public class Message {
    private int direction ;
    private String Message;

    public Message(int direction, String message) {
        this.direction = direction;
        Message = message;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
