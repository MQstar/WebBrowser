package com.demo.qx.webbrowser.custom;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by qx on 16/10/17.
 */

public class MyAppWebViewClient extends WebViewClient {
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        view.loadUrl("http://www.baidu.com/s?wd=" + request.getUrl());
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (request.getRequestHeaders().get("Content-Type").equals("text/html"))
            return super.shouldOverrideUrlLoading(view, request);
        else return false;
    }
}

