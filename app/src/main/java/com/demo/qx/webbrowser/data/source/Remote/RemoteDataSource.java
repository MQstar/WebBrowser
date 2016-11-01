package com.demo.qx.webbrowser.data.source.Remote;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Download;
import com.demo.qx.webbrowser.data.source.DataSource;
import com.demo.qx.webbrowser.data.WebPage;

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

    @Override
    public void getBookmarks(@NonNull LoadCallback loadCallback) {

    }

    @Override
    public void refreshBookmarks() {

    }

    @Override
    public void deleteAllHistory() {

    }

    @Override
    public void removeBookmarks(String address) {

    }

    @Override
    public void removeHistory(@NonNull WebPage webPage) {

    }

    @Override
    public void refreshHistory() {

    }

    @Override
    public void getHistory(@NonNull LoadCallback loadCallback) {

    }

    @Override
    public void addDownload(Download download) {

    }

    @Override
    public void refreshDownload() {

    }

    @Override
    public void getDownload(DownloadLoadCallback downloadLoadCallback) {

    }

    @Override
    public void pause(Download download) {

    }

    @Override
    public void resume(Download download) {

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
    public void startAll() {

    }

    @Override
    public void pauseAll() {

    }


    @Override
    public void addBookmarks(@NonNull WebPage webPage) {

    }

    @Override
    public void deleteAllBookmarks() {

    }


    @Override
    public void addHistory(@NonNull WebPage webPage) {

    }
}
