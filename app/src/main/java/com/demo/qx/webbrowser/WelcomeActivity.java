package com.demo.qx.webbrowser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by qx on 16/10/6.
 */

public class WelcomeActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 1000);//3秒跳转
    }
    private static final int GOTO_MAIN_ACTIVITY = 0;
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case GOTO_MAIN_ACTIVITY:
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        };
    };
}