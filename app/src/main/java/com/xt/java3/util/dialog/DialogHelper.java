package com.xt.java3.util.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by steve on 17-11-1.
 */

public class DialogHelper {

    public static void showEnsureDialog(Context ctx ,  String message, final Runnable runnable){
        AlertDialog dialog = new AlertDialog.Builder(ctx).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                runnable.run();
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage(message).show();
    }
}
