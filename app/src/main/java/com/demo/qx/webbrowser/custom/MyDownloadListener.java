package com.demo.qx.webbrowser.custom;

import android.util.Log;
import android.webkit.DownloadListener;

/**
 * Created by qx on 16/10/31.
 */
public class MyDownloadListener implements DownloadListener {
    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Log.e("url",url);
        Log.e("userAgent",userAgent);
        Log.e("contentDisposition",contentDisposition);
        Log.e("mimetype",mimetype);
        Log.e("contentLength",contentLength+"");
    }
}