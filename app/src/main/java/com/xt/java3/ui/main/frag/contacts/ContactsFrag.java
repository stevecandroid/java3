package com.xt.java3.ui.main.frag.contacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.xt.java3.R;
import com.xt.java3.User;
import com.xt.java3.ui.adapter.FriendListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by steve on 17-10-24.
 */

public class ContactsFrag extends Fragment implements ContactsContract.View {

    private List<User> allFriends = new ArrayList<>();
    private ContactsContract.Presenter mPresenter = new ContactsPresenter(this);
    private FriendListAdapter adapter;
    public static final String ONLINE = "u:";
    public static final String OFFLINE = "d:";

    @BindView(R.id.friends_list)
    RecyclerView friendsList ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts,null);
        ButterKnife.bind(this,view);
        EventBus.getDefault().register(this);
        init();
        mPresenter.getfriends();
        mPresenter = new ContactsPresenter(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init(){
        adapter = new FriendListAdapter(this,allFriends);
        friendsList.setAdapter(adapter);
        friendsList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onGetFriendlist(String friendsids) {
        String[] ids = friendsids.split("\\|");
        Log.e("ContactsFrag",friendsids);
        for(String id : ids){
            Log.e("ContactsFrag",id);
            mPresenter.getfrienDetail(id);
        }
    }

    @Override
    public void onGetFriendsFail(Throwable e) {
        Log.e("ContactsFrag","FAIL " + e.toString());
    }

    @Override
    public void onGetFriendsDetail(List<User> users) {
       if(users!= null && users.size() > 0){
           allFriends.add(users.get(0));
           adapter.notifyDataSetChanged();
       }
    }

    @Override
    public void onDeleteSuccess(int pos) {
        ToastUtils.showShort("删除成功");
        allFriends.remove(pos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteFail(Throwable t) {
        ToastUtils.showShort("删除失败"+t.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(String msg){
        switch(msg){
            case ContactsPresenter.ADD_FRIENDS:
                allFriends.clear();
                mPresenter.getfriends();
                break;
        }

        Log.e("ContactsFrag",msg);

        if(msg.startsWith(ONLINE)){
            String id = msg.substring(msg.indexOf(":")+1,msg.length());
            for(User user : allFriends){
                if(user.getId() == Integer.valueOf(id)){
                    user.setStatus(1);
                }
            }
            adapter.notifyDataSetChanged();
        }

        if(msg.startsWith(OFFLINE)){
            String id = msg.substring(msg.indexOf(":")+1,msg.length());
            for(User user : allFriends){
                if(user.getId() == Integer.valueOf(id)){
                    user.setStatus(0);
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    public ContactsContract.Presenter getPresenter() {
        return mPresenter;
    }
}
