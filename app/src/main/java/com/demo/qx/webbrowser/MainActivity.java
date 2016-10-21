package com.demo.qx.webbrowser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.qx.webbrowser.custom.MyWebView;

import static android.R.attr.description;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {
    MyWebView mWebView;
    EditText mEditText;
    TextView mShowAddress;
    TextView mRefresh;
    ProgressBar mProgressBar;
    boolean isLoading;
    boolean isTyping;
    Activity mActivity=this;

    final String HOME = "file:///android_asset/www/index1.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void onBackPressed() {
        if (isTyping) {
            hideEdit();
        } else if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void hideEdit() {
        isTyping = false;
        mShowAddress.setVisibility(View.VISIBLE);
        mRefresh.setVisibility(View.VISIBLE);
        mEditText.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        hideEdit();
        if (actionId == EditorInfo.IME_ACTION_GO && !TextUtils.isEmpty(v.getText())) {
            mWebView.loadUrl("http://www.baidu.com/s?wd=" + v.getText());
        }
        return true;
    }

    void initView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_window) {
                    Toast.makeText(mActivity,"windows", Toast.LENGTH_SHORT).show();

                } else if (menuItemId == R.id.action_new_web) {
                    Toast.makeText(mActivity,"new window", Toast.LENGTH_SHORT).show();
                }else if (menuItemId == R.id.action_forward) {
                    if (mWebView.canGoForward())
                    mWebView.goForward();
                }
                return true;
            }
        });
        mRefresh = (TextView) findViewById(R.id.button_refresh);
        mRefresh.setOnClickListener(this);
        mShowAddress = (TextView) findViewById(R.id.show_address);
        mShowAddress.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.address);
        mEditText.setOnEditorActionListener(this);
        mEditText.setSelectAllOnFocus(true);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mWebView = (MyWebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        final Activity activity = this;
        mWebView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                mShowAddress.setText(view.getUrl());
                if (progress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE) {
                    isLoading = true;
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    mRefresh.setBackgroundResource(R.mipmap.ic_clear_black_36dp);
                }

                mProgressBar.setProgress(progress);
                if (progress == 100) {
                    isLoading = false;
                    mProgressBar.setVisibility(ProgressBar.GONE);
                    mRefresh.setBackgroundResource(R.mipmap.ic_sync_black_24dp);
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getRequestHeaders().get("Content-Type").equals("text/html"))
                    return super.shouldOverrideUrlLoading(view, request);
                else return false;
            }
        });

        mWebView.loadUrl("https://www.baidu.com");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_refresh:
                if (isLoading)
                    mWebView.stopLoading();
                else
                    mWebView.reload();
                break;
            case R.id.show_address:
                isTyping = true;
                mShowAddress.setVisibility(View.GONE);
                mRefresh.setVisibility(View.GONE);
                mEditText.setText(mShowAddress.getText());
                mEditText.setVisibility(View.VISIBLE);
                mEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, 0);
                break;
            default:
                break;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideEdit();
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
}
