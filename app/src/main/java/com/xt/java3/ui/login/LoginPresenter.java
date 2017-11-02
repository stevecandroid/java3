package com.xt.java3.ui.login;

import android.util.Log;

import com.xt.java3.App;
import com.xt.java3.User;
import com.xt.java3.base.BaseError;
import com.xt.java3.modules.request.LoginBody;
import com.xt.java3.modules.response.BaseResponse;
import com.xt.java3.modules.response.LoginResponse;
import com.xt.java3.modules.response.SearchPeopleResopnse;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by steve on 17-10-22.
 */

public class LoginPresenter implements LoginContract.Presenter {


    public static final int LOGIN_SUCCESS = 0;
    public static final int PASSWORD_INCORRECT = 1;
    public static final int ACCOUNT_NOT_EXIST = 2;

    LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login(LoginBody request) {
        User.login(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchPeopleResopnse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                            view.onLoginStart();
                    }

                    @Override
                    public void onNext(SearchPeopleResopnse response) {
                        if (response.getUsers().size() == 1) {
                            view.onLoginSuccess(response.getUsers().get(0));
                        } else {
                            view.onLoginError(new RuntimeException("未知异常"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onLoginError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void judgeState() {
        User.judgeState().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchPeopleResopnse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        view.onLoginStart();
                    }

                    @Override
                    public void onNext(SearchPeopleResopnse value) {
                        view.onStateCallback(value.getUsers().get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onLoginError(new BaseError(-3));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
