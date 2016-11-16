package com.demo.qx.webbrowser.data.source.Remote;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Download;
import com.demo.qx.webbrowser.data.WebPage;
import com.demo.qx.webbrowser.data.source.DataSource;
import com.demo.qx.webbrowser.download.downloadUnity.DownloadManager;

/**
 * Created by qx on 16/10/25.
 */
public class RemoteDataSource implements DataSource {
    private static RemoteDataSource INSTANCE;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }
//Bookmarks
    @Override
    public void getBookmarks(@NonNull LoadCallback loadCallback) {

    }

    @Override
    public void addBookmarks(@NonNull WebPage webPage) {

    }

    @Override
    public void deleteAllBookmarks() {

    }

    @Override
    public void removeBookmarks(String address) {

    }
//History
    @Override
    public void addHistory(@NonNull WebPage webPage) {

    }

    @Override
    public void deleteAllHistory() {

    }

    @Override
    public void removeHistory(@NonNull WebPage webPage) {

    }

    @Override
    public void getHistory(@NonNull LoadCallback loadCallback) {

    }
    //Download
    @Override
    public void addDownload(Download download, DownloadManager downloadManager) {

    }

    @Override
    public void getDownload(DownloadLoadCallback downloadLoadCallback) {

    }

    @Override
    public void pause(@NonNull Download download, DownloadManager downloadManager) {

    }

    @Override
    public void resume(@NonNull Download download, DownloadManager downloadManager) {

    }

    @Override
    public void removeDownloadAndFile(Download download) {

    }

    @Override
    public void deleteAllDownload() {

    }

    @Override
    public void removeDownload(Download download) {

    }

    @Override
    public void startAll(DownloadManager downloadManager) {

    }

    @Override
    public void pauseAll(DownloadManager downloadManager) {

    }

    @Override
    public void refreshBookmarks() {
        //do nothing
    }
    @Override
    public void refreshHistory() {
        //do nothing
    }
    @Override
    public void refreshDownload() {
        //do nothing
    }
}
