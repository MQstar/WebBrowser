package com.demo.qx.webbrowser.data.source;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Download;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.List;

/**
 * Created by qx on 16/10/25.
 */

public interface DataSource {
    void getBookmarks(@NonNull LoadCallback loadCallback);

    void refreshBookmarks();

    void deleteAllHistory();

    void removeBookmarks(@NonNull String address);

    void removeHistory(@NonNull WebPage webPage);

    void refreshHistory();

    void getHistory(@NonNull LoadCallback loadCallback);

    void addDownload(@NonNull Download download);

    void refreshDownload();

    void getDownload(@NonNull DownloadLoadCallback downloadLoadCallback);

    void pause(@NonNull Download download);

    void resume(@NonNull Download download);

    void removeDownloadAndFile(@NonNull Download download);

    void deleteAllDownload();

    void removeDownload(@NonNull Download download);

    void startAll();

    void pauseAll();


    interface LoadCallback {

        void onLoaded(List<WebPage> webPages);

        void onDataNotAvailable();
    }
    interface DownloadLoadCallback {

        void onLoaded(List<Download> downloads);

        void onDataNotAvailable();
    }

    interface GetCallback {

        void onLoaded(WebPage webPage);

    }
    void addBookmarks(@NonNull WebPage webPage);


    void deleteAllBookmarks();

    void addHistory(@NonNull WebPage webPage);

}
