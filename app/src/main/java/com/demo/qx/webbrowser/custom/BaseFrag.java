package com.demo.qx.webbrowser.custom;

import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by qx on 16/10/24.
 */

public class BaseFrag extends Fragment implements View.OnTouchListener {

        protected float point_x, point_y; //手指按下的位置
        private boolean flag;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                point_x = event.getX();
                point_y = event.getY();
                flag = false;
            }

            return true;
        }
    }

