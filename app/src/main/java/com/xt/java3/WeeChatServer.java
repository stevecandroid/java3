package com.xt.java3;

import com.xt.java3.modules.response.SearchPeopleResopnse;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by steve on 17-10-24.
 */

public class WeeChatServer {

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
