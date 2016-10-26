package com.demo.qx.webbrowser;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import com.demo.qx.webbrowser.Utils.Injection;
import com.demo.qx.webbrowser.custom.MyWebView;

public class MainActivity extends AppCompatActivity {
    MyWebView mWebView;
    boolean isTyping;
    DisplayMetrics mDisplayMetrics;
    WebFragment mWebFragment;
    int index=1;
    private Presenter mPresenter;
    final String HOME = "file:///android_asset/www/index1.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDisplayMetrics=getResources().getDisplayMetrics();

        mWebFragment=
                (WebFragment) getSupportFragmentManager().findFragmentById(R.id.web_fragment);
        if (mWebFragment == null) {
            mWebFragment = WebFragment.newInstance(0,"https://www.baidu.com");
            addFragmentToActivity(getSupportFragmentManager(),mWebFragment, R.id.web_fragment);
        }
        mPresenter = new Presenter(Injection.provideTasksRepository(getApplicationContext()), mWebFragment);
        mWebView=mWebFragment.mWebView;
        Log.e("############", String.valueOf(mWebView==null));
    }

    private void addFragmentToActivity(@NonNull FragmentManager supportFragmentManager,@NonNull WebFragment webFragment, int web_fragment) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.add(web_fragment, webFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
       // if (isTyping) {
            //mWebFragment.hideEdit();
       // } else if (mWebView.canGoBack()) {
       //     mWebView.goBack();
       // } else {
            super.onBackPressed();
      //  }
    }

    void initView() {


        //mWebView.loadUrl("https://www.baidu.com");

    }

    private void getNewWebFrag(boolean add) {
        mWebFragment= WebFragment.newInstance(index,"https://www.baidu.com");
        index++;
        MyApp.sWebFragmentList.add(mWebFragment);
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        if (add)
            transaction.add(R.id.web_fragment, mWebFragment);
        else transaction.replace(R.id.web_fragment, mWebFragment);
        transaction.commit();
//        mWebView.setWebChromeClient(new MyWebChromeClient(this,mPresenter));
//        mWebView.setWebViewClient(new MyAppWebViewClient());
    }



    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                mWebFragment.hideEdit();
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    //截图
    private Bitmap captureWebView(WebView webView){
        Picture snapShot = webView.capturePicture();

        Bitmap bmp = null;
        int width = snapShot.getWidth();
        int height = (int) (width * 9 / 16);//默认16:9的设备比例，算出截屏的高

        if (width > 0 && height > 0)
        {
            bmp = Bitmap.createBitmap(width ,height , Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            snapShot.draw(canvas);

        }
        return bmp;
    }

    public void setNewWindow(WebView.WebViewTransport webViewTransport) {
        getNewWebFrag(false);
        mWebFragment.setTransport(webViewTransport);
    }
}
