package com.xt.java3.ui.main.frag.contacts;

import com.xt.java3.User;
import com.xt.java3.base.BaseView;

import java.util.List;

/**
 * Created by steve on 17-10-23.
 */

public interface ContactsContract {

    interface View extends BaseView<Presenter> {
        void onGetFriendlist(String friends);
        void onGetFriendsFail(Throwable e);
        void onGetFriendsDetail(List<User> users);
        void onDeleteSuccess(int pos);
        void onDeleteFail(Throwable e);
    }

    interface Presenter {
        void getfriends();
        void getfrienDetail(String id);
        void deleteFriend(int id , int pos);
    }
}
