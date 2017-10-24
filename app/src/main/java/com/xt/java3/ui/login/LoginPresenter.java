package com.xt.java3.ui.login;

import android.util.Log;

import com.xt.java3.App;
import com.xt.java3.User;
import com.xt.java3.modules.request.LoginBody;
import com.xt.java3.modules.response.BaseResponse;
import com.xt.java3.modules.response.LoginResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by steve on 17-10-22.
 */

public class LoginPresenter implements LoginContract.Presenter {


    public static final int LOGIN_SUCCESS = 0 ;
    public static final int PASSWORD_INCORRECT = 1;
    public static final int ACCOUNT_NOT_EXIST = 2;

    LoginContract.View view ;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login(LoginBody request){
        User.login(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse response) throws Exception {
                        if(response.getStatus() == LOGIN_SUCCESS){
                            view.onLoginSuccess(response.getUser());
                        }else{
                            view.onLoginError(new RuntimeException("未知异常"));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                            view.onLoginError(throwable);
                    }
                });

    }

    @Override
    public void judgeState() {
        App.client.resistent().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse response) throws Exception {
                        view.onStateCallback(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("LoginPresenter",throwable.toString());
                    }
                });
    }


}
