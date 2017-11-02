package com.xt.java3.util;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

/**
 * Created by steve on 17-10-27.
 */

public class ActivityLifeManager {
    private static Stack<Activity> activities = new Stack<>();

    private ActivityLifeManager() {
    }

    public static  void  push(Activity activity) {
        activities.push(activity);
        Log.e("ActivityLifeManager","push  "+activity.getLocalClassName());
    }

    public static  void pop() {
        if(!activities.isEmpty()){
            Activity act = activities.pop();
            Log.e("ActivityLifeManager","pop  " + act.getLocalClassName());
        }

    }

    public  static void clear() {

        while (!activities.isEmpty()) {
            Log.e("ActivityLifeManager  ",activities.size()+"size ");
            activities.pop().finish();
        }
    }
}
