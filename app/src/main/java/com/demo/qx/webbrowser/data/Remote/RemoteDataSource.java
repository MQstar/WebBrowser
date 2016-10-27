package com.demo.qx.webbrowser.data.Remote;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.DataSource;
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
    public void addBookmarks(@NonNull WebPage webPage) {

    }

    @Override
    public void deleteBookmarks(@NonNull String url) {

    }

    @Override
    public void addHistory(@NonNull WebPage webPage) {

    }
}
