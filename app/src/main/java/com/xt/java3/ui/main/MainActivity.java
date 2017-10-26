package com.xt.java3.ui.main;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xt.java3.App;
import com.xt.java3.R;
import com.xt.java3.modules.event.EventUser;
import com.xt.java3.modules.response.SearchPeopleResopnse;
import com.xt.java3.ui.login.LoginActivity;
import com.xt.java3.ui.main.frag.FragPagerAdaptaer;
import com.xt.java3.ui.main.frag.chat.ChatFrag;
import com.xt.java3.ui.main.frag.contacts.ContactsFrag;
import com.xt.java3.ui.profile.ForeProfileActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

    }

    private void init() {

        setSupportActionBar(toolbar);

        List<android.support.v4.app.Fragment> frags = new ArrayList<>();
        frags.add(new ChatFrag());
        frags.add(new ContactsFrag());
        viewPager.setAdapter(new FragPagerAdaptaer(getSupportFragmentManager(), frags));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("消息");
        tabLayout.getTabAt(1).setText("联系人");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);


        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                int id = -1 ;
                if(RegexUtils.isMatch("^[0-9]*$",query)){
                    id = Integer.parseInt(query);
                }


                App.client.search(id, query, query)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<SearchPeopleResopnse>() {
                            @Override
                            public void accept(SearchPeopleResopnse resopnse) throws Exception {
                                if (resopnse.getStatus() != 0 || resopnse.getUsers().size() <= 0) {
                                    throw new RuntimeException("找不到此人");
                                }
                            }
                        })
                        .subscribe(new Consumer<SearchPeopleResopnse>() {
                            @Override
                            public void accept(SearchPeopleResopnse resopnse) throws Exception {
                                MainActivity.this.startActivity(new Intent(MainActivity.this, ForeProfileActivity.class));
                                EventBus.getDefault().postSticky(new EventUser(resopnse.getUsers()));
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                ToastUtils.showShort("找不到此人");
                            }
                        });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search:
                break;
            case R.id.logout:
                App.cookieMgr.clear("Set-Cookie");
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }


        return true;

    }
}
