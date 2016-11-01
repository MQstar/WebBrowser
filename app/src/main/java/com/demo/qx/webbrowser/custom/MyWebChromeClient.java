package com.demo.qx.webbrowser.custom;

import android.os.Message;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.demo.qx.webbrowser.data.WebPage;
import com.demo.qx.webbrowser.home.WebActivity;
import com.demo.qx.webbrowser.home.WebContract;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {


        Toast.makeText(mWebActivity, message, Toast.LENGTH_SHORT).show();

        result.confirm();

        return true;
    }

    public void onProgressChanged(WebView view, int progress) {
        mPresenter.setAddress(view.getUrl());
        mPresenter.changeProgress(progress);
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String date = formatter.format(curDate);
        mPresenter.addHistory(new WebPage(view.getUrl(),view.getTitle(),date));

    }
}
