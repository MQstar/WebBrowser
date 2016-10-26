package com.demo.qx.webbrowser;

import android.app.Application;

import com.demo.qx.webbrowser.custom.MainFrag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qx on 16/10/21.
 */

public class MyApp extends Application {
    public static List<MainFrag> sFragList =new ArrayList<>();
    public static List<WebFragment> sWebFragmentList =new ArrayList<>();
    //public static  int sInitPage =1;  //初始化的界面个数
}
