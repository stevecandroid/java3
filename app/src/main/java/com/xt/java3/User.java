package com.xt.java3;

import com.xt.java3.base.BaseError;
import com.xt.java3.modules.request.LoginBody;
import com.xt.java3.modules.request.RegisterBody;
import com.xt.java3.modules.response.BaseResponse;
import com.xt.java3.modules.response.FriendsResponse;
import com.xt.java3.modules.response.LoginResponse;
import com.xt.java3.modules.response.SearchPeopleResopnse;
import com.xt.java3.modules.response.SearchRecordResponse;
import com.xt.java3.ui.login.LoginPresenter;
import com.xt.java3.ui.register.RegisterPresenter;

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

    public static Observable<SearchRecordResponse> getRecords(int to){
        return App.client.searchRecods(to).doOnNext(new Consumer<SearchRecordResponse>() {
            @Override
            public void accept(SearchRecordResponse response) throws Exception {
                if(response.getStatus() != 0){
                    throw new RuntimeException("错误");
                }
            }
        });
    }

    public static Observable<SearchPeopleResopnse> searchById(int id){
        return App.client.search(id,null,null).doOnNext(new Consumer<SearchPeopleResopnse>() {
            @Override
            public void accept(SearchPeopleResopnse resopnse) throws Exception {
                if(resopnse.getStatus() != 0){
                    throw new RuntimeException("查找失败");
                }
            }
        });
    }

}
