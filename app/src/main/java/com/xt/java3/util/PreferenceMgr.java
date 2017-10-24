package com.xt.java3.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.xt.java3.App;

import java.util.List;
import java.util.Map;

/**
 * Created by steve on 17-10-22.
 */

public class PreferenceMgr {


    private SharedPreferences preferences;

    public PreferenceMgr(String preferenceName){
         preferences = App.app.getSharedPreferences(preferenceName,Context.MODE_PRIVATE);
    }

    public synchronized void save(String key , Object o){
        SharedPreferences.Editor editor = preferences.edit();
        tansition_save(editor,key,o);
        editor.apply();
    }

    private void tansition_save(SharedPreferences.Editor editor, String key , Object o){
        if(o instanceof  Integer){
            editor.putInt(key, (Integer) o);
        }else if( o instanceof Boolean){
            editor.putBoolean(key, (Boolean) o);
        }else if (o instanceof Float) {
            editor.putFloat(key, (Float) o);
        }else if ( o instanceof Long){
            editor.putLong(key, (Long) o);
        }else {
            editor.putString(key,o.toString());
        }
    }

    public synchronized  <T> void saveAll(Map<String,T> datas){
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : datas.keySet()) {
            tansition_save(editor, key, datas.get(key));
        }
        editor.apply();
    }

    public synchronized void clear(String key){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
    }

    public synchronized  <T>  T get(String key , T o ){

        Object result;

        if(o instanceof  Integer){
            result = preferences.getInt(key, (Integer) o);
        }else if( o instanceof Boolean){
            result = preferences.getBoolean(key, (Boolean) o);
        }else if (o instanceof Float) {
            result = preferences.getFloat(key, (Float) o);
        }else if ( o instanceof Long){
            result = preferences.getLong(key, (Long) o);
        }else {
            result = preferences.getString(key,o.toString());
        }

        return (T) result;
    }



}
