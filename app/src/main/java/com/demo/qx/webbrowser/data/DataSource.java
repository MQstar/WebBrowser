package com.demo.qx.webbrowser.data;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by qx on 16/10/25.
 */

public interface DataSource {
    void getBookmarks(@NonNull LoadCallback loadCallback);

    void refreshBookmarks();

    void deleteAllHistory();


    interface LoadCallback {

        void onLoaded(List<WebPage> webPages);

        void onDataNotAvailable();
    }

    interface GetCallback {

        void onLoaded(WebPage webPage);

    }
    void addBookmarks(@NonNull WebPage webPage);

    void deleteBookmarks(@NonNull String url);

    void addHistory(@NonNull WebPage webPage);

}
