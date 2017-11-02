package com.xt.java3.ui.main.frag.contacts;

import com.blankj.utilcode.util.ToastUtils;
import com.xt.java3.User;
import com.xt.java3.modules.response.BaseResponse;
import com.xt.java3.modules.response.FriendsResponse;
import com.xt.java3.modules.response.SearchPeopleResopnse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by steve on 17-10-23.
 */

public class ContactsPresenter implements ContactsContract.Presenter {

    public static final String ADD_FRIENDS = "addFriends";


    private ContactsContract.View view;

    public ContactsPresenter(ContactsContract.View view) {
        this.view = view;
    }

    @Override
    public void getfriends() {
        User.getfriends().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FriendsResponse>() {
                               @Override
                               public void accept(FriendsResponse friendsResponse) throws Exception {
                                   view.onGetFriendlist(friendsResponse.getFriends());
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   view.onGetFriendsFail(throwable);
                               }
                           }
                );
    }

    @Override
    public void getfrienDetail(String id) {
        User.searchById(Integer.parseInt(id)).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchPeopleResopnse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SearchPeopleResopnse response) {
                        view.onGetFriendsDetail(response.getUsers());
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onGetFriendsFail(new RuntimeException("获取具体信息失败"));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void deleteFriend(int id , final int pos) {
        User.deleteFriend(id).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse value) {
                        if(value.getStatus() == 0 )
                            view.onDeleteSuccess(pos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onDeleteFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
