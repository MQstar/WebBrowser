package com.demo.qx.webbrowser;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.demo.qx.webbrowser.custom.MainFrag;

import java.util.Iterator;

/**
 * Created by qx on 16/10/24.
 */

public class MultWindows extends AppCompatActivity implements MainFrag.RemoveListener {
    ViewPager mViewPager;
    ImageView mImageView;
    MyAdapter mMyAdapter;
    LayoutInflater inflater;
    LinearLayout mLinearLayout;
    PropertyValuesHolder pvhX;
    PropertyValuesHolder pvhY;
    ObjectAnimator scale;
    Bitmap bmp;
    private DisplayMetrics mDisplayMetrics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    void initView() {
        setContentView(R.layout.mult_window);
        inflater = getLayoutInflater();
        bmp= (Bitmap) getIntent().getExtras().get("bitmap");
        mDisplayMetrics = getResources().getDisplayMetrics();
        mLinearLayout = (LinearLayout) findViewById(R.id.page);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        //for (int i = 0; i < MyApp.sInitPage; i++) {
            MainFrag tmp = newFragment();
            MyApp.sFragList.add(tmp);
       // }
        mMyAdapter = new MyAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mMyAdapter);
        mViewPager.setOffscreenPageLimit(3);
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.add_page:
                MainFrag tmp = newFragment();
                MyApp.sFragList.add(mViewPager.getCurrentItem() + 1, tmp);

                mMyAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                break;
           /* case R.id.small:
                mViewPager.setPageMargin(40);
                pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f);
                pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f);
                scale = ObjectAnimator.ofPropertyValuesHolder(mViewPager, pvhX, pvhY);
                scale.setDuration(50);
                scale.start();
                RelativeLayout.LayoutParams Rlparam = new RelativeLayout.LayoutParams(mDisplayMetrics.widthPixels * 8 / 10, mDisplayMetrics.heightPixels * 8 / 10);
                Rlparam.addRule(CENTER_IN_PARENT);
                mLinearLayout.setLayoutParams(Rlparam);
                break;
            case R.id.big:
                mViewPager.setPageMargin(0);
                pvhX = PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1f);
                pvhY = PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1f);
                scale = ObjectAnimator.ofPropertyValuesHolder(mViewPager, pvhX, pvhY);
                scale.setDuration(50);
                scale.start();
                RelativeLayout.LayoutParams Rlparam2 = new RelativeLayout.LayoutParams(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
                Rlparam2.addRule(CENTER_IN_PARENT);
                mLinearLayout.setLayoutParams(Rlparam2);
                break;*/
        }
    }

    @NonNull
    private MainFrag newFragment() {
        MainFrag tmp = new MainFrag();
        tmp.setId(MyApp.sFragList.size());
        tmp.setRemoveListener(this);
        tmp.setBmp(bmp);
        return tmp;
    }

    @Override
    public void remove(int id) {
        if (MyApp.sFragList.size()==0)return;
        Iterator<MainFrag> iterator = MyApp.sFragList.iterator();
        while(iterator.hasNext()){
            MainFrag temp = iterator.next();
            if (temp.getid() == id)
                iterator.remove();   //注意这个地方
        }
            mMyAdapter.notifyDataSetChanged();
    }
}

 /*extends PagerAdapter{
    List<View> mViewList;
    public MyAdapter(List<View> viewList) {
        mViewList=viewList;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup parent = (ViewGroup)mViewList.get(position).getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeViewAt(position);
    }
}
*/