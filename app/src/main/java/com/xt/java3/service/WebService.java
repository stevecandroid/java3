package com.xt.java3.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.xt.java3.Constant;
import com.xt.java3.R;
import com.xt.java3.modules.event.Message;
import com.xt.java3.ui.main.MainActivity;
import com.xt.java3.util.PreferenceMgr;
import com.xt.java3.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import retrofit2.http.Url;

/**
 * Created by steve on 17-10-25.
 */

public class WebService extends Service {

    private WebSocketClient conn;
    private ServiceBinder binder = new ServiceBinder();

    public static final int CHAT_PREFIX = Message.CHAT;
    public static String ONLINE = "u:";
    public static String OFFLINE = "d:";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("WebService","onbind");
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e("WebService","Service created");


    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        Log.e("WebService","bind server");
        return super.bindService(service, conn, flags);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        if(conn == null){
            int id ;

            if(intent == null || (id = intent.getIntExtra("id",-1)) == -1 ){
                id = new PreferenceMgr("lastid").get("id", -1);
            }

            if(id == -1) {
                stopSelf();
            }

            try {
                conn = new WebSocketClient(new URI(Constant.WS+id),new Draft_6455()) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {

                    }

                    @Override
                    public void onMessage(String message) {

                        if(message.startsWith(ONLINE) || message.startsWith(OFFLINE)){
                            EventBus.getDefault().post(message);
                        }

                        Message msg = new Gson().fromJson(message,Message.class);
                        msg.setDirection(0);
                        switch(msg.getType()){
                            case CHAT_PREFIX :
                                EventBus.getDefault().post(msg);
                                manager.notify(1,createNotification(msg.getMessage()));
                                break;
                            default:break;
                        }

                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        try {
                            conn.connectBlocking();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception ex) {

                    }
                };

                conn.connectBlocking();
            } catch (URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }
        }


        return START_STICKY;

    }

    public class ServiceBinder extends Binder{
        public WebService getService(){
            return WebService.this;
        }
    }

    public void send(String message){
        conn.send(message);
    }

    @Override
    public void onDestroy() {
        Log.e("WebService","server stop");
        conn.close();
        super.onDestroy();
    }

    NotificationManager manager ;
    private Notification createNotification(String msg){

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent,0);
        return new NotificationCompat.Builder(this,"web")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setContentTitle("收到一条信息")
                .setContentText((msg))
                .setShowWhen(true)
                .setAutoCancel(true)
                .setVibrate(new long[]{0,500,300,500,400,700})
                .build();
    }
}
