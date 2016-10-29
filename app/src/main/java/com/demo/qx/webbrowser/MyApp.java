package com.demo.qx.webbrowser;

import android.app.Application;

import com.demo.qx.webbrowser.multiwindow.MultiFragment;
import com.demo.qx.webbrowser.home.WebFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qx on 16/10/21.
 */

public class MyApp extends Application {
    public static List<MultiFragment> sMultiFragments =new ArrayList<>();
    public static List<WebFragment> sWebFragmentList =new ArrayList<>();
}
