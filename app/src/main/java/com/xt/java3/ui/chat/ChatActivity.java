package com.xt.java3.ui.chat;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xt.java3.App;
import com.xt.java3.R;
import com.xt.java3.User;
import com.xt.java3.network.chat.WeeChat;
import com.xt.java3.ui.adapter.Message;
import com.xt.java3.ui.adapter.RecycleChatAdapter;
import com.xt.java3.util.BitmapUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.message)
    EditText message;

    @BindView(R.id.avatar)
    ImageView avatar;

    @BindView(R.id.info)
    TextView info;

    @BindView(R.id.recycle_chat)
    RecyclerView recycleChat;

    @BindView(R.id.send)
    Button send;

    User user ;

    List<Message> messages = new ArrayList<>();

    RecycleChatAdapter adapter = new RecycleChatAdapter(messages);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN ,sticky = true)
    public void getUser(User user){
        this.user = user;
        avatar.setImageBitmap( BitmapUtils.base64ToBitmap(user.getAvatar()));
        info.setText(user.getNickname());
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getMessge(Message msg){
        messages.add(msg);
        adapter.notifyItemInserted(messages.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.send)
    public void onClick(View view ){
        messages.add(new Message(1,message.getText().toString()));
        App.weeChat.send(praseMessage(message.getText().toString(), String.valueOf(App.mUser.getId()),new String[]{String.valueOf(user.getId())}));
        message.setText("");
        adapter.notifyItemInserted(messages.size());
    }

    private void init(){

        recycleChat.setLayoutManager(new LinearLayoutManager(this));
        recycleChat.setAdapter(adapter);

    }

    private String praseMessage(String message, String from , String[] to ){
        return "from'"+ App.mUser.getId()+"'"+"to>" + to[0] + "<"+ message;
    }
}
