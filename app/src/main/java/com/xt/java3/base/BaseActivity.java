package com.xt.java3.base;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xt.java3.service.WebService;
import com.xt.java3.util.pic.AlbumPicker;
import com.xt.java3.util.pic.PermissionsWrapper;
import com.xt.java3.util.pic.PhotoTaker;
import com.xt.java3.util.ActivityLifeManager;

/**
 * Created by steve on 17-10-27.
 */

public class BaseActivity extends AppCompatActivity {

    public AlbumPicker albumPicker = AlbumPicker.Companion.with(this);
    public PhotoTaker photoTaker = PhotoTaker.Companion.with(this);
    public PermissionsWrapper.PermissionMgr permissionMgr = PermissionsWrapper.PermissionMgr.Companion.with(this);

    protected WebService webService;

    protected ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.e("ChatActivity","CONNECT");
            webService = ((WebService.ServiceBinder)binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            webService = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityLifeManager.pop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLifeManager.push(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        albumPicker.onActivityResult(requestCode,resultCode,data);
        photoTaker.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionMgr.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
