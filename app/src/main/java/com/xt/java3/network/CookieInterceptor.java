package com.xt.java3.network;

import android.util.Log;

import com.xt.java3.App;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by steve on 17-10-22.
 */

public class CookieInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        String cookie;
        Request request;
        Response response;

        if(!(cookie = App.cookieMgr.get("Set-Cookie","")).equals("")){
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Cookie",cookie);
            request = builder.build();
            Log.e("CookieInterceptor",cookie);
        }else{
            request = chain.request();
        }

        response = chain.proceed(request);

        if(response.header("Set-Cookie") != null){
            App.cookieMgr.clear("Set-Cookie");
            App.cookieMgr.save("Set-Cookie",response.header("Set-Cookie"));
        }

        return response;
    }
}
