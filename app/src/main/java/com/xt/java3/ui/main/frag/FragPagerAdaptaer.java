package com.xt.java3.ui.main.frag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by steve on 17-10-24.
 */

public class FragPagerAdaptaer extends FragmentPagerAdapter {

    private List<Fragment> fragments ;

    public FragPagerAdaptaer(FragmentManager fm , List<Fragment> fragmentList) {
        super(fm);
        this.fragments = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
