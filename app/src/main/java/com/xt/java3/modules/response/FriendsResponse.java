package com.xt.java3.modules.response;

/**
 * Created by steve on 17-10-23.
 */


public class FriendsResponse extends BaseResponse {

    private String friends;

    public FriendsResponse(int status , String friends) {
        super(status);
        this.friends = friends;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }
}
