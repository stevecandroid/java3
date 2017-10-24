package com.xt.java3.ui.register;

import com.xt.java3.base.BaseView;
import com.xt.java3.modules.request.RegisterBody;

/**
 * Created by steve on 17-10-22.
 */

public interface RegisterContract {

    interface View extends BaseView<Presetner> {
        void onRegisterSuccess();
        void onRegisterError(Throwable e);
    }

    interface Presetner {
        void register(RegisterBody request);
    }
}
