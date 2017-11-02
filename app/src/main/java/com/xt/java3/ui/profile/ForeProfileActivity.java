package com.xt.java3.ui.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xt.java3.R;
import com.xt.java3.User;
import com.xt.java3.modules.event.EventUser;
import com.xt.java3.ui.adapter.SearchAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForeProfileActivity extends AppCompatActivity {

    private List<User> users = new ArrayList<>();
    private SearchAdapter adapter = new SearchAdapter(users);

    @BindView(R.id.search_result)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fore_profile);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //得到搜索得到的用户的信息,并显示列表
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getUserList(EventUser users){
        if(users.users.size() > 0) {
            this.users.addAll(users.users);
            adapter.notifyDataSetChanged();
        }
    }

}
