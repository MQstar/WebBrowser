package com.demo.qx.webbrowser.multiwindow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.demo.qx.webbrowser.MyApp;
import com.demo.qx.webbrowser.R;
import com.demo.qx.webbrowser.home.WebFragment;

import java.util.Iterator;

/**
 * Created by qx on 16/10/24.
 */

public class MultiWindow extends AppCompatActivity implements MultiFragment.RemoveListener, MultiFragment.OpenListener, View.OnClickListener {
    ViewPager mViewPager;
    MyAdapter mMyAdapter;
    ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    void initView() {
        setContentView(R.layout.multiwindow);
        mImageView= (ImageView) findViewById(R.id.add_page);
        mImageView.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        MyApp.sFragList.clear();
        for (int i = 0; i < MyApp.sWebFragmentList.size(); i++) {
            MultiFragment tmp = MultiFragment.getInstance(MyApp.sWebFragmentList.get(i).getAId(),MyApp.sWebFragmentList.get(i).mCurrentTitle,MyApp.sWebFragmentList.get(i).mBitmap,this,this);
            MyApp.sFragList.add(tmp);
        }
        mMyAdapter = new MyAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mMyAdapter);
        mViewPager.setPageMargin(40);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void remove(long hCode) {
        if (MyApp.sFragList.size()==0)return;
        Iterator<MultiFragment> iterator = MyApp.sFragList.iterator();
        while(iterator.hasNext()){
            MultiFragment temp = iterator.next();
            if (temp.getHCode() == hCode)
            {iterator.remove();
            break;}
        }
            mMyAdapter.notifyDataSetChanged();
        Iterator<WebFragment> iterator2 = MyApp.sWebFragmentList.iterator();
        while(iterator.hasNext()){
            WebFragment temp = iterator2.next();
            if (temp.getAId() == hCode)
            {iterator2.remove();
                break;}
        }
        if (MyApp.sFragList.size()==0)noFragmentRemain();
    }
    void noFragmentRemain(){
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void open(long hCode) {
        int index=0;
        for (int i = 0; i < MyApp.sWebFragmentList.size(); i++) {
            if (MyApp.sWebFragmentList.get(i).getAId()==hCode){
                index=i;
                break;
        }}
        Intent intent = getIntent();
        intent.putExtra("ID", index);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_page:
                noFragmentRemain();
                break;
        }
    }
}