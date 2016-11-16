package com.demo.qx.webbrowser.download.downloadUnity;

import android.os.Handler;
import android.os.Message;

import com.demo.qx.webbrowser.MyApp;
import com.demo.qx.webbrowser.data.Download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutorService;

/**
 * Created by qx on 16/10/31.
 */

public class DownloadTask implements ConnectThread.ConnectListener {
    private final Handler mHandler;
    private final Download mDownload;
    private final ExecutorService mExecutorService;
    private ConnectThread mConnectThread;
    private volatile boolean isPaused;
    private volatile boolean isCancel;

    public DownloadTask(Download download, Handler handler, ExecutorService executorService) {
        mDownload = download;
        mHandler = handler;
        mExecutorService=executorService;
    }

    public void start() {
        mDownload.status= Download.DownloadStatus.connecting;
        notifyData(mDownload,DownloadService.NOTIFY_CONNECTING);
        mConnectThread =new ConnectThread(mDownload.url,this);
        mExecutorService.execute(mConnectThread);







        notifyData(mDownload,DownloadService.NOTIFY_DOWNLOADING);
        HttpURLConnection conn = null;
        InputStream input = null;
        RandomAccessFile randomAccessFile = null;
        OutputStream outputStream = null;
        try {
            conn = (HttpURLConnection) new URL(mDownload.url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            if (mDownload.contentLength == -1) {
                mDownload.contentLength = conn.getContentLength();
            }
            if (mDownload.contentLength <= 0) {
                return;
            }
            File dir = new File(MyApp.dir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 构建文件对象
            File file = new File(dir, mDownload.name);
            if (206 == conn.getResponseCode()) {
                DownThread[] threads = new DownThread[MyApp.thread_number];
                long currentPartSize = mDownload.contentLength / MyApp.thread_number + 1;
                //设置下载的文件的大小
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.setLength(mDownload.contentLength);
                randomAccessFile.close();
                //新建每个线程的RandomAccessFile和开线程
                for (int i = 0; i < MyApp.thread_number; i++) {
                    long startPos = i * currentPartSize;
                    RandomAccessFile currentPart = new RandomAccessFile(file, "rw");
                    currentPart.seek(startPos);
                    threads[i] = new DownThread(startPos, currentPartSize, currentPart, mDownload.url);
                    threads[i].start();
                }
            } else {
                outputStream = new FileOutputStream(file);
                input = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = input.read(buffer)) != -1) {
                    if (isPaused||isCancel){
                        notifyData(mDownload,DownloadService.NOTIFY_PAUSE_CANCELLED);
                    }
                    outputStream.write(buffer, 0, len);
                    mDownload.currentLength += len;
                    notifyData(mDownload,DownloadService.NOTIFY_UPDATING);
                }
                mDownload.status = Download.DownloadStatus.complete;
            }
            notifyData(mDownload,DownloadService.NOTIFY_COMPLETED);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyData(Download download,int what) {
        Message msg = mHandler.obtainMessage();
        msg.what=what;
        msg.obj = download;
        mHandler.sendMessage(msg);
    }

    public void pause() {
        isPaused = true;
        if (mConnectThread!=null&&mConnectThread.isRunning()){
            mConnectThread.cancel();
        }
    }

    public void cancel() {
        isCancel=true;
        if (mConnectThread!=null&&mConnectThread.isRunning()){
            mConnectThread.cancel();
        }
    }

    @Override
    public void onConnected(boolean isSupportRange, int totalLength) {
        mDownload.supportRange=isSupportRange;
        mDownload.contentLength=totalLength;
        if (mDownload.supportRange){
//TODO multi
        }else {
//TODO single
        }
    }

    @Override
    public void onError(String message) {
        mDownload.status= Download.DownloadStatus.error;
        notifyData(mDownload,DownloadService.NOTIFY_ERROR);
    }


    class DownThread extends Thread {
        private long startPos;
        private long currentPartSize;
        private RandomAccessFile currentPart;
        private int length;
        private String path;

        public DownThread(long startPos, long currentPartSize, RandomAccessFile currentPart, String path) {
            this.startPos = startPos;
            this.currentPartSize = currentPartSize;
            this.currentPart = currentPart;
            this.path = path;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(path);
                //获取输入流和跳过不需要该线程下载的部分
                InputStream inputStream = url.openStream();
                inputStream.skip(startPos);
                byte[] buffer = new byte[1024];
                int hasRead = 0;
                //下载到该文件的大小超过指定的大小或文件结束为止
                while (length < currentPartSize
                        && (hasRead = inputStream.read(buffer)) != -1) {
                    currentPart.write(buffer, 0, hasRead);
                    length += hasRead;
                }
                currentPart.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
