package com.xt.java3;

import android.util.Log;

import com.xt.java3.base.BaseError;
import com.xt.java3.modules.request.LoginBody;
import com.xt.java3.modules.request.RegisterBody;
import com.xt.java3.modules.response.BaseResponse;
import com.xt.java3.modules.response.FriendsResponse;
import com.xt.java3.modules.response.LoginResponse;
import com.xt.java3.ui.login.LoginPresenter;
import com.xt.java3.ui.register.RegisterPresenter;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by steve on 17-10-22.
 */

public class User {


    private int id;
    private int age;
    private String email;
    private String nickname;
    private String avatar;

    private static volatile User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public static Observable<LoginResponse> login(LoginBody request){
        return App.client.login(request).doOnNext(new Consumer<LoginResponse>() {
            @Override
            public void accept(LoginResponse response) throws Exception {
                if(response.getStatus() != LoginPresenter.LOGIN_SUCCESS){
                    throw new BaseError(response.getStatus());
                }
            }
        });
    }

    public static Observable<BaseResponse> register(RegisterBody request){
        return App.client.register(request).doOnNext(new Consumer<BaseResponse>() {
            @Override
            public void accept(BaseResponse response) throws Exception {
                if(response.getStatus() != RegisterPresenter.REGISTER_SUCCES){
                    throw new BaseError(response.getStatus());
                }
            }
        });
    }

    public static Observable<FriendsResponse> getfriends(){
        return App.client.getfriends().doOnNext(new Consumer<FriendsResponse>() {
            @Override
            public void accept(FriendsResponse friendsResponse) throws Exception {
                if(friendsResponse.getStatus() != 0){
                    throw new BaseError(friendsResponse.getStatus());
                }
            }
        });
    }

}
