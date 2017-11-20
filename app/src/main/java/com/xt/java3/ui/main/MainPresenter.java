package com.xt.java3.ui.main;

import com.xt.java3.App;
import com.xt.java3.modules.User;
import com.xt.java3.base.BaseResponse;
import com.xt.java3.modules.response.SearchPeopleResopnse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by steve on 17-10-27.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view ;

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void queryPeople(int id, String email, String nickname) {
        App.client.search(id, email, nickname)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<SearchPeopleResopnse>() {
                    @Override
                    public void accept(SearchPeopleResopnse resopnse) throws Exception {
                        if (resopnse.getStatus() != 0 || resopnse.getUsers().size() <= 0) {
                            throw new RuntimeException("找不到此人");
                        }
                    }
                })
                .subscribe(new Consumer<SearchPeopleResopnse>() {
                    @Override
                    public void accept(SearchPeopleResopnse resopnse) throws Exception {
                        view.onQuerySuccess(resopnse.getUsers());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.onQueryError(throwable);
                    }
                });
    }

    @Override
    public void modifyUser(User user ) {
        User.modifyUser(user).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse value) {
                        view.onUploadSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onUploadError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
