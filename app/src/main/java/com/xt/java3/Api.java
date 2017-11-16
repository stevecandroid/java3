package com.xt.java3;

import com.xt.java3.modules.request.LoginBody;
import com.xt.java3.modules.request.RegisterBody;
import com.xt.java3.modules.response.BaseResponse;
import com.xt.java3.modules.response.FriendsResponse;
import com.xt.java3.modules.response.LoginResponse;
import com.xt.java3.modules.response.SearchPeopleResopnse;
import com.xt.java3.modules.response.SearchRecordResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by steve on 17-10-21.
 */

public interface Api {

    @GET("/login")
    Observable<LoginResponse> resistent();

    @POST("/login")
    Observable<LoginResponse> login(@Body LoginBody body) ;

    @POST("/register")
    Observable<BaseResponse> register(@Body RegisterBody body);

    @GET("/friends")
    Observable<FriendsResponse> getfriends();

    @POST("/friends")
    @FormUrlEncoded
    Observable<BaseResponse> modifyFriends(@Query("action") int action , @Field(value = "id",encoded = true) int id);

    @GET("/people/search")
    Observable<SearchPeopleResopnse> search(@Query("id") int id , @Query("email") String email ,
                                            @Query(value = "nickname",encoded = true) String nickname);

    @GET("/record/search")
    Observable<SearchRecordResponse> searchRecods(@Query("to") int to );

    @POST("/user/modify")
    Observable<BaseResponse> uploadProfile(@Body User user);

    @POST("/record/delete")
    @FormUrlEncoded
    Observable<BaseResponse> deleteRecord(@Field("u") int to , @Field("t") long time , @Field("d") int direction);
}

