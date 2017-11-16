package com.xt.java3.ui.chat;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xt.java3.App;
import com.xt.java3.Constant;
import com.xt.java3.R;
import com.xt.java3.User;

import com.xt.java3.base.BaseActivity;
import com.xt.java3.service.WebService;
import com.xt.java3.modules.event.Message;
import com.xt.java3.ui.adapter.RecycleChatAdapter;

import com.xt.java3.util.Utils;
import com.xt.java3.util.pic.bitmap.BitmapUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends BaseActivity implements ChatContract.View {

    @BindView(R.id.message)
    EditText message;

    @BindView(R.id.avatar)
    CircleImageView avatar;

    @BindView(R.id.info)
    TextView info;

    @BindView(R.id.recycle_chat)
    RecyclerView recycleChat;

    @BindView(R.id.send)
    Button send;

    //聊天的对象
    private User user ;

    //聊天的内容
    private List<Message> messages = new ArrayList<>();

    private RecycleChatAdapter adapter ;

    private ChatContract.Presenter mPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);


        //开启后台服务,接收信息
        bindService(new Intent(this, WebService.class),conn, Context.BIND_AUTO_CREATE);

        mPresenter = new ChatPresenter(this);
        mPresenter.getMessage(user.getId());

        adapter = new RecycleChatAdapter(messages , user.getId());
        init();

    }

    /**
     * Evenbus在选择聊天对象的时候发送该对象
     * @param user 得到聊天的对象信息
     *             设置聊天对象的图片以及名字
     */
    @Subscribe(threadMode = ThreadMode.MAIN ,sticky = true)
    public void getUser(User user){
        this.user = user;
        Glide.with(this).load(Constant.IP+"avatar?id="+user.getId()).diskCacheStrategy(DiskCacheStrategy.NONE).into(avatar);
        info.setText(user.getNickname());
    }

    /**
     *
     * @param msg 服务器发过来的消息
     * 后台服务回调onMesaage 利用EvenBus 发送消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessge(Message msg){
        messages.add(msg);
        adapter.notifyItemInserted(messages.size());
        recycleChat.smoothScrollToPosition(messages.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.send)
    public void onClick(View view ){
        //记录发送的时间
        long currentTime = System.currentTimeMillis();
        //空内容不允许发送
        if(!message.getText().toString().equals("")) {
            //添加聊天的一项记录
            messages.add(new Message(currentTime, 1, message.getText().toString()));
            //发送聊天信息至服务器
            webService.send(Utils.encodeMessage(message.getText().toString(),
                    String.valueOf(App.mUser.getId()),
                    new String[]{String.valueOf(user.getId())},currentTime));
            //重置聊天框
            message.setText("");
            //更新UI
            adapter.notifyItemInserted(messages.size());
            recycleChat.smoothScrollToPosition(messages.size());
        }else{
            ToastUtils.showShort("empty mesasge is not allow !");
        }
    }

    /**
     * 初始化
     */
    private void init(){
        LinearLayoutManager llm  = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recycleChat.setLayoutManager(llm);
        recycleChat.setAdapter(adapter);
    }

    /**
     * 第一次进入活动调用mPresenter.getMessage(user.getId()); 的回调函数
     * 获取聊天记录
     * @param messages
     */
    @Override
    public void onGetMessages(List<Message> messages) {
//        int start = this.messages.size();
        this.messages.addAll(messages);
        adapter.notifyDataSetChanged();
    }

}
