package com.xt.java3.ui.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;



import com.blankj.utilcode.util.ToastUtils;
import com.xt.java3.R;
import com.xt.java3.ui.main.frag.ChatFrag;
import com.xt.java3.ui.main.frag.ContactsFrag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private void init(){

        setSupportActionBar(toolbar);

        List<android.support.v4.app.Fragment> frags = new ArrayList<>();
        frags.add(new ChatFrag());
        frags.add(new ContactsFrag());
        viewPager.setAdapter(new FragPagerAdaptaer(getSupportFragmentManager(),frags));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("消息");
        tabLayout.getTabAt(1).setText("联系人");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.search:break;
            case R.id.logout:break;
        }


        return true;

    }
}
