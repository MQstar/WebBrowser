package com.demo.qx.webbrowser.custom;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.demo.qx.webbrowser.MyApp;

/**
 * Created by qx on 16/10/24.
 */
class MyAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private FragmentManager fm;
    public MyAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context = context;
        this.fm=fm;
    }

    @Override
    public Fragment getItem(int position) {
        return MyApp.sFragList.get(position);
    }

    @Override
    public int getCount() {
        return MyApp.sFragList.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
