package com.xt.java3.modules.response;

import com.xt.java3.base.BaseResponse;
import com.xt.java3.modules.User;

import java.util.List;

/**
 * Created by steve on 17-10-24.
 */

public class SearchPeopleResopnse  extends BaseResponse {

    private List<User> friends ;

    public SearchPeopleResopnse(int status , List<User> friends) {
        super(status);
        this.friends = friends;
    }

    public List<User> getUsers() {
        return friends;
    }

    public void setUsers(List<User> users) {
        this.friends = users;
    }
}

