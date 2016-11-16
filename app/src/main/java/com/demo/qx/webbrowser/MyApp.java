package com.demo.qx.webbrowser;

import android.app.Application;

import com.demo.qx.webbrowser.home.WebFragment;
import com.demo.qx.webbrowser.multiwindow.MultiFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qx on 16/10/21.
 */

public class MyApp extends Application {
    public static final int RESULT_NO_FRAGMENT_REMAIN = 911119;
    public static final int RESULT_NO_BACK = 119911;
    public static int thread_number;
    public static int task_number;
    public static String dir;
    public static List<MultiFragment> sMultiFragments =new ArrayList<>();
    public static List<WebFragment> sWebFragmentList =new ArrayList<>();
}
