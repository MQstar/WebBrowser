package com.demo.qx.webbrowser.custom;

import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.demo.qx.webbrowser.MainActivity;
import com.demo.qx.webbrowser.MainContract;

/**
 * Created by qx on 16/10/24.
 */
public class MyWebChromeClient extends WebChromeClient {
    MainContract.Presenter mPresenter;
    MainActivity mMainActivity;

    public MyWebChromeClient(MainActivity mainActivity, MainContract.Presenter presenter) {
        mMainActivity = mainActivity;
        mPresenter = presenter;
    }

    @Override
    public boolean onCreateWindow(WebView view, final boolean dialog,
                                  final boolean userGesture, final Message resultMsg) {
        if (userGesture) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            mMainActivity.setNewWindow(transport);
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
