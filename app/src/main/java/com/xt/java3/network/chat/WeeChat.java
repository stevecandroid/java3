package com.xt.java3.network.chat;

import com.xt.java3.Constant;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by steve on 17-10-23.
 */

public class WeeChat {

    private WebSocketClient connection ;

    private static WeeChat webSocket;

    private WeeChat(String id) {
        try {
            connection = new WebSocketClient(new URI(Constant.WS+id), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    if(onOpenListener!=null){
                        onOpenListener.onOpen(handshakedata);
                    }
                }

                @Override
                public void onMessage(String message) {
                    if(onMessageListener!= null){
                        onMessageListener.onMessage(message);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    if(onCloseListener != null){
                        onCloseListener.onClose();
                    }
                }

                @Override
                public void onError(Exception ex) {
                    if(onErrorListener!=null){
                        onErrorListener.onError(ex);
                    }
                }
            };
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static WeeChat getInstance(String id){
        if(webSocket == null){
            synchronized (WeeChat.class){
                if(webSocket == null)
                    webSocket =new WeeChat(id);
            }
        }
        return webSocket;
    }

    public void send(String msg){
        connection.send(msg);
    }

    public void connect(){
        try {
            connection.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {

            connection.closeBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public interface onOpenListener{
        void onOpen(ServerHandshake handshakedata);
    }

    public interface onMessageListener{
        void onMessage(String message);
    }

    public interface onCloseListener{
        void onClose();
    }

    public interface onErrorListener{
        void onError(Exception ex);
    }

    onOpenListener onOpenListener;
    onMessageListener onMessageListener;
    onCloseListener onCloseListener;
    onErrorListener onErrorListener;

    public void setOnOpenListener(WeeChat.onOpenListener onOpenListener) {
        this.onOpenListener = onOpenListener;
    }

    public void setOnMessageListener(WeeChat.onMessageListener onMessageListener) {
        this.onMessageListener = onMessageListener;
    }

    public void setOnCloseListener(WeeChat.onCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public void setOnErrorListener(WeeChat.onErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }
}
