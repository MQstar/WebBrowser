package com.demo.qx.webbrowser.download.downloadUnity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.demo.qx.webbrowser.MyApp;
import com.demo.qx.webbrowser.data.Download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import static com.demo.qx.webbrowser.data.Download.DownloadStatus.cancelled;
import static com.demo.qx.webbrowser.data.Download.DownloadStatus.paused;

/**
 * Created by qx on 16/10/31.
 */

public class DownloadService extends Service {

    public static final int NOTIFY_DOWNLOADING=1;
    public static final int NOTIFY_UPDATING=2;
    public static final int NOTIFY_PAUSE_CANCELLED=3;
    public static final int NOTIFY_COMPLETED=4;
    public static final int NOTIFY_ERROR = 5;
    public static final int NOTIFY_CONNECTING = 6;

    private DataChanger mDataChanger;
    private ExecutorService mExecutors;
    private Map<Integer,DownloadTask> mDownloadMap=new HashMap<>();
    private LinkedBlockingDeque<Download> mWaitingQueue = new LinkedBlockingDeque<>();
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NOTIFY_PAUSE_CANCELLED:
                case NOTIFY_COMPLETED:
                    checkNext();
                    break;
            }
            DataChanger.getInstance().postStatus((Download) msg.obj);
        }
    };

    private void checkNext() {
        Download newDownload=mWaitingQueue.poll();
        if (newDownload!=null)
            startDownload(newDownload);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutors = Executors.newCachedThreadPool();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Download download= (Download) intent.getSerializableExtra("download");
        int action=intent.getIntExtra("action",-1);
        doAction(action,download);
        return super.onStartCommand(intent, flags, startId);
    }

    private void doAction(int action, Download download) {

        switch (action){
            case Constants.ADD:
                addDownload(download);
            break;
            case Constants.PAUSE:
                pauseDownload(download);
                break;
            case Constants.RESUME:
                resumeDownload(download);
                break;
            case Constants.CANCEL:
                cancelDownload(download);
                download.status= Download.DownloadStatus.downloading;
                DataChanger.getInstance().postStatus(download);
                break;
        }
    }

    private void addDownload(Download download){
        if (mDownloadMap.size() >= MyApp.task_number){
            mWaitingQueue.offer(download);
            download.status= Download.DownloadStatus.waiting;
        }else
            startDownload(download);
    }

    private void cancelDownload(Download download) {
        DownloadTask temp= mDownloadMap.remove(download.hashCode());
        if (temp!=null)
        temp.cancel();
        else {
            mWaitingQueue.remove(download);
            download.status= cancelled;
            DataChanger.getInstance().postStatus(download);
        }
    }

    private void resumeDownload(Download download) {
        addDownload(download);
    }

    private void startAll(){
        ArrayList<Download> mRecoverAll=DataChanger.getInstance().queryAllRecoverableDownload();
        if (mRecoverAll!=null){
            for (Download download : mRecoverAll) {
                addDownload(download);
            }
        }
    }

    private void pauseAll(){
        while (mWaitingQueue.iterator().hasNext()){
            Download download=mWaitingQueue.poll();
            download.status=paused;
            DataChanger.getInstance().postStatus(download);
        }
        for (Map.Entry<Integer, DownloadTask> taskEntry : mDownloadMap.entrySet()) {
            taskEntry.getValue().pause();
        }
        mDownloadMap.clear();
    }
    private void pauseDownload(Download download) {
        DownloadTask temp= mDownloadMap.remove(download.hashCode());
        if (temp!=null)
            temp.pause();
        else {
            mWaitingQueue.remove(download);
            download.status= paused;
            DataChanger.getInstance().postStatus(download);
        }
    }

    private void startDownload(Download download) {
        DownloadTask task=new DownloadTask(download,mHandler,mExecutors);
        mDownloadMap.put(download.hashCode(),task);
        task.start();
    }
    /*private boolean isExcutable(){
        long tmp=System.currentTimeMillis();
        if (tmp-mLastOperateTime>MIN_OPERATE_INTERVAL)
            return true;
        return false;
    }*/
}
