package com.tany.base.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * 作者: Tany
 * 时间: 2017/2/14 16:13
 * 描述:
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private Fragment[] fragments;
    private String[] titles;

    public MyViewPagerAdapter(FragmentManager fm, Context context, Fragment[] fragments, String[] titles) {
        super(fm);
        this.mContext = context;
        this.fragments = fragments;
        this.titles=titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
