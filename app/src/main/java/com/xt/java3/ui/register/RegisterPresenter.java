package com.xt.java3.ui.register;

import com.xt.java3.modules.User;
import com.xt.java3.modules.request.RegisterBody;
import com.xt.java3.base.BaseResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by steve on 17-10-22.
 */

public class RegisterPresenter implements  RegisterContract.Presetner {

    public static final int REGISTER_FAIL = -1 ;

    private RegisterContract.View view ;


    RegisterPresenter(RegisterContract.View view) {
        this.view = view;
    }

    @Override
    public void register(RegisterBody request){
        User.register(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
                        if(response.getStatus() == REGISTER_FAIL){
                            view.onRegisterError(new RuntimeException("注册失败"));

                        }else{
                           view.onRegisterSuccess(response.getStatus());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

    }

}
