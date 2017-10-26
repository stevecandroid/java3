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

import com.xt.java3.R;
import com.xt.java3.User;
import com.xt.java3.ui.adapter.FriendListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by steve on 17-10-24.
 */

public class ContactsFrag extends Fragment implements ContactsContract.View {

    List<User> allFriends = new ArrayList<>();
    ContactsContract.Presenter mPresenter = new ContactsPresenter(this);
    FriendListAdapter adapter;

    @BindView(R.id.friends_list)
    RecyclerView friendsList ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts,null);
        ButterKnife.bind(this,view);

        init();

        mPresenter = new ContactsPresenter(this);
        mPresenter.getfriends();
        return view;
    }

    private void init(){
        adapter = new FriendListAdapter(allFriends);
        friendsList.setAdapter(adapter);
        friendsList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
           adapter.notifyItemInserted(allFriends.size());
       }

    }

}
