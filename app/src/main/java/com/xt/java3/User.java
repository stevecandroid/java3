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
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by steve on 17-10-22.
 */

public class User implements Cloneable{

    public static boolean firstLogin = true;
    private int id;
    private int age;
    private String email;
    private String nickname;
    private String avatar;
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static Observable<SearchPeopleResopnse> login(LoginBody request){
        return App.client.login(request).doOnNext(new Consumer<LoginResponse>() {
            @Override
            public void accept(LoginResponse response) throws Exception {
                if(response.getStatus() != LoginPresenter.LOGIN_SUCCESS){
                    throw new BaseError(response.getStatus());
                }
            }
        }).flatMap(new Function<LoginResponse, ObservableSource<SearchPeopleResopnse>>() {
            @Override
            public ObservableSource<SearchPeopleResopnse> apply(LoginResponse response) throws Exception {
                int id = response.getUserId();
                return App.client.search(id, null, null);
            }
        }).doOnNext(new Consumer<SearchPeopleResopnse>() {
            @Override
            public void accept(SearchPeopleResopnse response) throws Exception {
                if (response.getStatus() != 0) {
                    throw new RuntimeException("登录查询出错");
                }
            }
        });
    }

    public static Observable<BaseResponse> register(RegisterBody request){
        return App.client.register(request).doOnNext(new Consumer<BaseResponse>() {
            @Override
            public void accept(BaseResponse response) throws Exception {
                if(response.getStatus() == RegisterPresenter.REGISTER_FAIL){
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

    public static Observable<BaseResponse> modifyUser(User user ){
        return App.client.uploadProfile(user).doOnNext(new Consumer<BaseResponse>() {
            @Override
            public void accept(BaseResponse response) throws Exception {
                if(response.getStatus() != 0 ){
                    throw new RuntimeException("error response code");
                }
            }
        });
    }

    public static Observable<SearchPeopleResopnse> judgeState(){

        return App.client.resistent().doOnNext(new Consumer<LoginResponse>() {
            @Override
            public void accept(LoginResponse response) throws Exception {
                if(response.getStatus() != 0 || response.getUserId() == -1){
                    throw new RuntimeException("登录凭证过时");
                }
            }
        }).flatMap(new Function<LoginResponse, ObservableSource<SearchPeopleResopnse>>() {
            @Override
            public ObservableSource<SearchPeopleResopnse> apply(LoginResponse response) throws Exception {
                return User.searchById(response.getUserId());
            }
        }).doOnNext(new Consumer<SearchPeopleResopnse>() {
            @Override
            public void accept(SearchPeopleResopnse resopnse) throws Exception {
                if(resopnse.getStatus() != 0 || resopnse.getUsers().size() < 1)
                    throw new RuntimeException("登录异常");
            }
        });

    }

    public static Observable<BaseResponse> deleteFriend(int id){
        return App.client.modifyFriends(Constant.ACTION_DELETE,id).doOnNext(new Consumer<BaseResponse>() {
            @Override
            public void accept(BaseResponse response) throws Exception {
                if(response.getStatus() != 0 ){
                    throw new RuntimeException("删除失败");
                }
            }
        });
    }

    public static Observable<BaseResponse> deleteRecord(int to , long time , int direction ){
        return App.client.deleteRecord(to,time,direction).doOnNext(new Consumer<BaseResponse>() {
            @Override
            public void accept(BaseResponse baseResponse) throws Exception {
                if(baseResponse.getStatus() != 0 ){
                    throw new RuntimeException("删除失败");
                }
            }
        });
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        //String 不可以变, int 不是对象 因此浅复制没问题
        User user = (User) super.clone();
        return user;
    }

    @Override
    public String toString() {
        return this.getId() + ":" + this.getNickname() +":" + this.getEmail();
    }
}
