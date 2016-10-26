package com.demo.qx.webbrowser.custom;

import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.demo.qx.webbrowser.home.WebActivity;
import com.demo.qx.webbrowser.home.WebContract;

/**
 * Created by qx on 16/10/24.
 */
public class MyWebChromeClient extends WebChromeClient {
    WebContract.Presenter mPresenter;
    WebActivity mWebActivity;

    public MyWebChromeClient(WebActivity webActivity, WebContract.Presenter presenter) {
        mWebActivity = webActivity;
        mPresenter = presenter;
    }

    @Override
    public boolean onCreateWindow(WebView view, final boolean dialog,
                                  final boolean userGesture, final Message resultMsg) {
        if (userGesture) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            mWebActivity.setNewWindow(transport);
            resultMsg.sendToTarget();
            return true;
        }
        return false;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        mPresenter.setTitle(title);
    }

    public void onProgressChanged(WebView view, int progress) {
        mPresenter.setAddress(view.getUrl());
        mPresenter.changeProgress(progress);

    }
}
