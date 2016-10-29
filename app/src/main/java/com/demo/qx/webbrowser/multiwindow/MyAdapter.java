package com.demo.qx.webbrowser.multiwindow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.demo.qx.webbrowser.MyApp;

/**
 * Created by qx on 16/10/24.
 */
class MyAdapter extends FragmentStatePagerAdapter {

    private FragmentManager fm;
    public MyAdapter(FragmentManager fm) {
        super(fm);
        this.fm=fm;
    }

    @Override
    public Fragment getItem(int position) {
        return MyApp.sMultiFragments.get(position);
    }

    @Override
    public int getCount() {
        return MyApp.sMultiFragments.size();
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
