package com.xt.java3.ui.chat;

import com.xt.java3.base.BaseView;
import com.xt.java3.modules.event.Message;

import java.util.List;

/**
 * Created by steve on 17-10-25.
 */

public interface ChatContract {

    interface View extends BaseView<Presenter>{
        void onGetMessages(List<Message> messages);
    }

    interface Presenter {
        void getMessage(int id );
    }
}
