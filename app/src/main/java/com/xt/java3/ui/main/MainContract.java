package com.xt.java3.ui.main;

import com.xt.java3.User;
import com.xt.java3.base.BaseView;

import java.util.List;

/**
 * Created by steve on 17-10-23.
 */

public interface MainContract {

    interface View extends BaseView<Presenter> {
        void onGetFriendlist(String friends);
        void onGetFriendsFail(Throwable e);
        void onGetFriendsDetail(List<User> users);
    }

    interface Presenter {
        void getfriends();
        void getfrienDetail(String id);
    }
}
