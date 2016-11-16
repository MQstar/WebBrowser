package com.demo.qx.webbrowser.download.downloadUnity;

import android.content.Context;
import android.content.Intent;

import com.demo.qx.webbrowser.data.Download;

/**
 * Created by qx on 16/10/31.
 */

public class DownloadManager {
    private static DownloadManager mInstance;
    private final Context mContext;

    private DownloadManager(Context context) {
        mContext = context;
    }
    public static synchronized DownloadManager getInstance(Context context){
        if (mInstance==null){
            mInstance=new DownloadManager(context);
        }
        return mInstance;
    }
    public void add(Download download){
        Intent intent=new Intent(mContext,DownloadService.class);
        intent.putExtra(Constants.DOWNLOAD,download);
        intent.putExtra(Constants.ACTION_ADD,Constants.ADD);
        mContext.startService(intent);
    }
    public void pause(Download download){
        Intent intent=new Intent(mContext,DownloadService.class);
        intent.putExtra(Constants.DOWNLOAD,download);
        intent.putExtra(Constants.ACTION_PAUSE,Constants.PAUSE);
        mContext.startService(intent);}
    public void resume(Download download){
        Intent intent=new Intent(mContext,DownloadService.class);
        intent.putExtra(Constants.DOWNLOAD,download);
        intent.putExtra(Constants.ACTION_RESUME,Constants.RESUME);
        mContext.startService(intent);}
    public void cancel(Download download){
        Intent intent=new Intent(mContext,DownloadService.class);
        intent.putExtra(Constants.DOWNLOAD,download);
        intent.putExtra(Constants.ACTION_CANCEL,Constants.CANCEL);
        mContext.startService(intent);}
    public void addObserver(DataWatcher watcher){
        DataChanger.getInstance().addObserver(watcher);
    }
    public void removeObserver(DataWatcher watcher){
        DataChanger.getInstance().deleteObserver(watcher);
    }
}
