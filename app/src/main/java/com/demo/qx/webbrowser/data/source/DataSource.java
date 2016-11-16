package com.demo.qx.webbrowser.data.source;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Download;
import com.demo.qx.webbrowser.data.WebPage;
import com.demo.qx.webbrowser.download.downloadUnity.DownloadManager;

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

    void addDownload(@NonNull Download download, DownloadManager downloadManager);

    void refreshDownload();

    void getDownload(@NonNull DownloadLoadCallback downloadLoadCallback);

    void pause(@NonNull Download download, DownloadManager downloadManager);

    void resume(@NonNull Download download, DownloadManager downloadManager);

    void removeDownloadAndFile(@NonNull Download download);

    void deleteAllDownload();

    void removeDownload(@NonNull Download download);

    void startAll(DownloadManager downloadManager);

    void pauseAll(DownloadManager downloadManager);

    interface LoadCallback {

        void onLoaded(List<WebPage> webPages);

        void onDataNotAvailable();
    }
    interface DownloadLoadCallback {

        void onLoaded(List<Download> downloads);

        void onDataNotAvailable();
    }

    interface GetCallback {

        void onLoaded(Download download);

    }
    void addBookmarks(@NonNull WebPage webPage);


    void deleteAllBookmarks();

    void addHistory(@NonNull WebPage webPage);

}
