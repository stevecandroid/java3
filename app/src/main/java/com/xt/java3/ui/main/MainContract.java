package com.xt.java3.ui.main;

import com.xt.java3.modules.User;
import com.xt.java3.base.BaseView;

import java.util.List;

/**
 * Created by steve on 17-10-27.
 */

public interface MainContract {

    interface View extends BaseView<Presenter>{
        void onQuerySuccess(List<User> users);
        void onQueryError(Throwable e);
        void onUploadSuccess();
        void onUploadError(Throwable e);
    }

    interface Presenter {
        void queryPeople(int id , String email ,String nickname);
        void modifyUser(User user);
    }
}
