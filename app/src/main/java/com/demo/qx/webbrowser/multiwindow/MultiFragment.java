package com.demo.qx.webbrowser.multiwindow;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.qx.webbrowser.R;

/**
 * Created by qx on 16/10/24.
 */

public class MultiFragment extends Fragment implements View.OnClickListener {
    TextView mShowTitle;
    ImageView mImageView;
    String mTitle;
    RemoveListener mRemoveListener;
    OpenListener mOpenListener;
    TextView mClose;
    long hCode;
    Bitmap bmp;

    public static MultiFragment getInstance(long hCode, String title, Bitmap bmp, RemoveListener removeListener,OpenListener openListener){
        MultiFragment multiFragment =new MultiFragment();
        multiFragment.sethCode(hCode);
        multiFragment.setTitle(title);
        multiFragment.setBmp(bmp);
        multiFragment.setRemoveListener(removeListener);
        multiFragment.setOpenListener(openListener);
        return multiFragment;
    }
    public void sethCode(long hCode) {
        this.hCode = hCode;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }
    public void setRemoveListener(RemoveListener removeListener) {
        mRemoveListener = removeListener;
    }

    public void setOpenListener(OpenListener openListener) {
        mOpenListener = openListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_item, container, false);
        mClose = (TextView) view.findViewById(R.id.close_page);
        mClose.setOnClickListener(this);
        mShowTitle = (TextView) view.findViewById(R.id.show_title);
        mShowTitle.setText(mTitle);
        mImageView= (ImageView) view.findViewById(R.id.screen);
        mImageView.setImageBitmap(bmp);
        mImageView.setOnClickListener(this);
        return view;
    }


    public long getHCode() {
        return hCode;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.screen:
                mOpenListener.open(hCode);
                break;
            case R.id.close_page:
                mRemoveListener.remove(hCode);
                break;
        }
    }
    public interface RemoveListener{
        void remove(long hCode);
    }
    public interface OpenListener{
        void open(long hCode);
    }
}
