package com.demo.qx.webbrowser.custom;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.qx.webbrowser.MyApp;
import com.demo.qx.webbrowser.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qx on 16/10/24.
 */

public class MainFrag extends BaseFrag implements View.OnClickListener {
    private RelativeLayout mRelativeLayout;
    private DisplayMetrics mDisplayMetrics;
    private LinearLayout mDel;
    private TextView mShowTitle;
    ImageView mImageView;
    RemoveListener mRemoveListener;
    int id;
    Bitmap bmp;

    public void setId(int id) {
        this.id = id;
    }

    public void setRemoveListener(RemoveListener removeListener) {
        mRemoveListener = removeListener;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.up_del, container, false);
        mDisplayMetrics = getResources().getDisplayMetrics();
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.relative);
        mRelativeLayout.setOnTouchListener(this);
        mDel = (LinearLayout) view.findViewById(R.id.del_page);
        mDel.setOnClickListener(this);
        mShowTitle = (TextView) view.findViewById(R.id.show_title);
        mImageView= (ImageView) view.findViewById(R.id.screen);
        mImageView.setImageBitmap(bmp);
        return view;
    }

    private float mov_x, mov_y; //相对于手指移动了的位置
    private int left, right, top, bottom;
    private List<int[]> positionlist = new ArrayList<>();

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouch(v, event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            positionlist.clear();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            mov_x = event.getX() - super.point_x;
            mov_y = event.getY() - super.point_y;
            left = mRelativeLayout.getLeft();
            right = mRelativeLayout.getRight();
            top = mRelativeLayout.getTop();
            bottom = mRelativeLayout.getBottom();
            if (Math.abs(mDisplayMetrics.widthPixels - mRelativeLayout.getWidth()) > 5) {
                mRelativeLayout.layout(left, top + (int) mov_y, right, bottom + (int) mov_y);
                int[] position = {left, top + (int) mov_y, right, bottom + (int) mov_y};
                positionlist.add(position);
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (positionlist.size() >= 2) {
                if (MyApp.sFragList.size() > 1 && Math.abs(positionlist.get(positionlist.size() - 1)[1]) > mRelativeLayout.getWidth() / 2) {
                    delAnime();
                    //TODO delet fragment
                    mRemoveListener.remove(id);
                    return true;
                }
            }
            for (int i = positionlist.size() - 1; i >= 0; i--) {
                mRelativeLayout.layout(positionlist.get(i)[0], positionlist.get(i)[1], positionlist.get(i)[2], positionlist.get(i)[3]);
            }
            mRelativeLayout.layout(0, 0, mRelativeLayout.getWidth(), mRelativeLayout.getHeight());
        }
        return true;
    }

    //删除动画
    private void delAnime() {
        if (MyApp.sFragList.size() <= 1) {
            return;
        }
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.01f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.01f);
        ObjectAnimator scalexy = ObjectAnimator.ofPropertyValuesHolder(mRelativeLayout, pvhX, pvhY);
        ObjectAnimator scale = ObjectAnimator.ofFloat(mRelativeLayout, "translationY", 0, -2500);
        scale.setDuration(200);
        scalexy.setDuration(200);
        scale.start();
        scalexy.start();
    }

    public int getid() {
        return id;
    }

    @Override
    public void onClick(View v) {
        delAnime();
        //TODO delete fragment
        mRemoveListener.remove(id);
    }
    public interface RemoveListener{
        void remove(int id);
    }
}
