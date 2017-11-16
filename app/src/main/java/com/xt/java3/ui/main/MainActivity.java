package com.xt.java3.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.xt.java3.Constant;
import com.xt.java3.util.pic.bitmap.BitmapUtil;
import com.xt.java3.App;
import com.xt.java3.R;
import com.xt.java3.User;
import com.xt.java3.base.BaseActivity;
import com.xt.java3.modules.event.EventUser;
import com.xt.java3.service.WebService;
import com.xt.java3.ui.login.LoginActivity;
import com.xt.java3.ui.main.frag.FragPagerAdaptaer;
import com.xt.java3.ui.main.frag.contacts.ContactsFrag;
import com.xt.java3.ui.profile.ForeProfileActivity;
import com.xt.java3.util.PreferenceMgr;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends BaseActivity implements  MainContract.View{

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.navigation)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    CircleImageView circleHead;
    TextView id ;

    MainContract.Presenter mPresenter;
    private ContactsFrag contactsFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindService(new Intent(this, WebService.class),conn, Context.BIND_AUTO_CREATE);

        mPresenter = new MainPresenter(this);

        ButterKnife.bind(this);

        initView();

    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }

    private void initView() {

        setSupportActionBar(toolbar);

        //初始化ViewParger和TabLayout
        List<android.support.v4.app.Fragment> frags = new ArrayList<>();
        contactsFrag =  new ContactsFrag();
        frags.add(contactsFrag);
        viewPager.setAdapter(new FragPagerAdaptaer(getSupportFragmentManager(), frags));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("联系人");

        //初始化侧拉菜单headerView
        View view = navigationView.getHeaderView(0);
        circleHead = view.findViewById(R.id.nav_avatar);

//        circleHead.setImageBitmap(BitmapUtil.Companion.base64ToBitmap(App.mUser.getAvatar()));
        Glide.with(this).load(Constant.IP+"avatar?id="+App.mUser.getId()).into(circleHead);
        id = view.findViewById(R.id.nav_id);
        id.setText(App.mUser.getNickname());


        //测拉菜单点击事件
        navigationView.setCheckedItem(R.id.nav_logout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                int id = item.getItemId();
                switch (id){
                    case R.id.nav_logout:
                        logout();
                        break;
                }
                return true;
            }
        });

        circleHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumPicker.selectedPicAndHandle(new Function1<String, Unit>() {
                    @Override
                    public Unit invoke(String s) {
                        try {
                            Bitmap bitmap =
                                    BitmapUtil.Companion.compressByQuality(
                                            BitmapFactory.decodeFile(s),(1024*1024*2));

                            String base64 =  BitmapUtil.Companion.bitmapToBase64(bitmap);

                            circleHead.setImageBitmap(bitmap);

                            User user = (User) App.mUser.clone();
                            user.setAvatar(base64);

                            Log.e("MainActivity",user.toString());
                            mPresenter.modifyUser(user);

                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
            }
        });

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
                mPresenter.queryPeople(id,query,query);
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
                logout();
                break;
        }
        return true;

    }

    private void logout(){

        App.cookieMgr.clear("Set-Cookie");
        new PreferenceMgr("lastid").clear("id");
        webService.stopSelf();

        Intent activityIntent = new Intent(this, LoginActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);
    }

    @Override
    public void onQuerySuccess(List<User> users) {
        //查询成功,启动活动显示查询到的信息
        MainActivity.this.startActivity(new Intent(MainActivity.this, ForeProfileActivity.class));
        //发送查询到的信息到下一个活动
        EventBus.getDefault().postSticky(new EventUser(users));
    }

    @Override
    public void onQueryError(Throwable e) {
        ToastUtils.showShort("查询失败:" + "找不到此人");
    }

    @Override
    public void onUploadSuccess() {
        ToastUtils.showShort("修改成功");
    }

    @Override
    public void onUploadError(Throwable e) {
        ToastUtils.showShort("修改失败");
    }
}
