package com.xt.java3;

import android.app.Application;
import com.blankj.utilcode.util.Utils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xt.java3.network.CookieInterceptor;
import com.xt.java3.util.PreferenceMgr;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by steve on 17-10-20.
 */

public class App extends Application{
  // 0 收 1 发
    public static PreferenceMgr cookieMgr;
    public static App app;
    public static Api client;
    public static User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        cookieMgr = new PreferenceMgr("cookie");

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .addInterceptor(new CookieInterceptor())
                .build();

        client = new Retrofit.Builder().client(okHttpClient).baseUrl(Constant.IP)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(Api.class);

        Utils.init(this);

    }



}
