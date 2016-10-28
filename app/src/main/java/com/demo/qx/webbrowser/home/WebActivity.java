package com.demo.qx.webbrowser.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.qx.webbrowser.MyApp;
import com.demo.qx.webbrowser.R;
import com.demo.qx.webbrowser.bookmarks.BookmarksActivity;
import com.demo.qx.webbrowser.download.DownloadActivity;
import com.demo.qx.webbrowser.history.HistoryActivity;
import com.demo.qx.webbrowser.utils.Injection;

import static com.demo.qx.webbrowser.utils.ActivityUtils.addFragmentToActivity;

public class WebActivity extends AppCompatActivity {
    //DisplayMetrics mDisplayMetrics;
    WebFragment mWebFragment;
    private WebPresenter mPresenter;
    final String HOME = "file:///android_asset/www/index1.html";
    Intent intent;
    private long exitTime = 0;
    private WebActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = WebActivity.this;
        intent = getIntent();
        //mDisplayMetrics=getResources().getDisplayMetrics();
        if (savedInstanceState == null) processExtraData();
    }

    private void processExtraData() {
        mWebFragment =
                (WebFragment) getSupportFragmentManager().findFragmentById(R.id.web_fragment);
        if (mWebFragment == null) {
            if (intent == null || intent.getStringExtra("URL") == null)
                mWebFragment = WebFragment.newInstance("https://www.baidu.com");
            else
                mWebFragment = WebFragment.newInstance(intent.getStringExtra("URL"));
            addFragmentToActivity(getSupportFragmentManager(), mWebFragment, R.id.web_fragment);
        }
        mPresenter = new WebPresenter(Injection.provideTasksRepository(getApplicationContext()), mWebFragment);
    }

    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.intent=getIntent();
        processExtraData();
    }*/

    @Override
    public void onBackPressed() {
        // if (isTyping) {
        //mWebFragment.hideEdit();
        // } else if (mWebView.canGoBack()) {
        //     mWebView.goBack();
        // } else {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                getNewWebFragment(data.getStringExtra("URL"));
                break;
            default:
                break;
        }
    }

    private void getNewWebFragment(@Nullable String url) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(mWebFragment);
        if (url == null)
            mWebFragment = WebFragment.newInstance("https://www.baidu.com");
        else
            mWebFragment = WebFragment.newInstance(url);
        MyApp.sWebFragmentList.add(mWebFragment);
        transaction.add(R.id.web_fragment, mWebFragment);
        transaction.commitAllowingStateLoss();
        mPresenter = new WebPresenter(Injection.provideTasksRepository(getApplicationContext()), mWebFragment);
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
   /* private Bitmap captureWebView(WebView webView){
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
    }*/

    public void setNewWindow(WebView.WebViewTransport webViewTransport) {
        getNewWebFragment(null);
        mWebFragment.setTransport(webViewTransport);
    }

    public void openBookmarks() {
        startActivityForResult(new Intent(mActivity, BookmarksActivity.class), 1);
    }

    public void openHistory() {
        startActivityForResult(new Intent(mActivity, HistoryActivity.class), 2);
    }

    public void openDownload() {
        startActivityForResult(new Intent(mActivity, DownloadActivity.class), 3);
    }

}
