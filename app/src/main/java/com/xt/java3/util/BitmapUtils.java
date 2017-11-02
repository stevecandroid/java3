package com.xt.java3.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.xt.java3.App;
import com.xt.java3.R;

/**
 * Created by steve on 17-10-24.
 */

public class BitmapUtils {
    public static Bitmap base64ToBitmap(String raw){
        try {
            byte[] bytes = Base64.decode(raw, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }catch (Exception e ){

            return BitmapFactory.decodeResource(App.app.getResources(), R.drawable.errorhead);
        }
    }
}
