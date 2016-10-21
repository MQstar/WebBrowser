package com.demo.qx.webbrowser.custom;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by qx on 16/10/17.
 */

public class MyAppWebViewClient extends WebViewClient {
  /* The shouldOverrideUrlLoading method is called whenever the WebView is about to load a URL.
     This implementation checks for the String "html5rocks.com" at the end of the host name of the URL.*/
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // If the string exists, the method returns false, which tells the platform not to override the URL,
        // but to load it in the WebView.
        if(Uri.parse(url).getHost().length()!=0) {
            return false;
        }
        //For any other hostname, the method makes a request to the system to open the URL.
        //It does this by creating a new Android Intent and using it to launch a new activity.
        //Returning true at the end of the method prevents the URL from being loaded into the WebView.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }
}
