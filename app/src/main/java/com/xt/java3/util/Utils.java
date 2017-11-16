package com.xt.java3.util;

import com.xt.java3.App;

/**
 * Created by steve on 17-10-25.
 */

public class Utils {

    public static String CHAT_PREFIX = "c:";

    public static String parseMessage(String msg){
        int start = msg.indexOf("<");
        if(start == -1 ) return msg;

        return msg.substring(start+1 , msg.length());
    }

    public static String encodeMessage(String message, String from , String[] to ,long sendTime){
            return CHAT_PREFIX+"t"+sendTime+"from'"+ App.mUser.getId()+"'"+"to>" + to[0] + "<"+ message;
    }
    // c://at:2541324213from'id'to>id,id,id,id,id,id,id,id<Message 聊天发送的格式
}
