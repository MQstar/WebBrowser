package com.demo.qx.webbrowser.data.source;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Download;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qx on 16/10/25.
 */

public class Repository implements DataSource {
    private static Repository INSTANCE = null;
    boolean mBookmarksCacheIsDirty = false;
    boolean mDownloadCacheIsDirty = false;
    boolean mHistoryCacheIsDirty = false;
    private final DataSource mRemoteDataSource;
    private final DataSource mLocalDataSource;
    Map<String, WebPage> mCachedBookmarks;
    Map<String, Download> mCachedDownload;
    Map<String, WebPage> mCachedHistory;

    private Repository(@NonNull DataSource remoteDataSource,
                       @NonNull DataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    public static Repository getInstance(DataSource remoteDataSource, DataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getBookmarks(@NonNull  final LoadCallback callback) {
        if (mCachedBookmarks != null && !mBookmarksCacheIsDirty) {
            callback.onLoaded(new ArrayList<>(mCachedBookmarks.values()));
            return;
        }

        if (mBookmarksCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getBookmarksFromRemoteDataSource(callback);
        } else {
            mLocalDataSource.getBookmarks(new LoadCallback() {
                @Override
                public void onLoaded(List<WebPage> webPages) {
                    refreshBookmarksCache(webPages);
                    callback.onLoaded(new ArrayList<>(mCachedBookmarks.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }

            });
        }
    }

    @Override
    public void refreshBookmarks() {
        mBookmarksCacheIsDirty = true;
    }

    @Override
    public void addBookmarks(WebPage webPage) {
        mRemoteDataSource.addBookmarks(webPage);
        mLocalDataSource.addBookmarks(webPage);

        if (mCachedBookmarks == null) {
            mCachedBookmarks = new LinkedHashMap<>();
        }
        mCachedBookmarks.put(webPage.getUrl(), webPage);
    }


    @Override
    public void deleteAllBookmarks() {
        mLocalDataSource.deleteAllBookmarks();

        if (mCachedBookmarks == null) {
            mCachedBookmarks = new LinkedHashMap<>();
        }
        mCachedBookmarks.clear();
    }

    @Override
    public void removeBookmarks(String address) {
        mRemoteDataSource.removeBookmarks(address);
        mLocalDataSource.removeBookmarks(address);
        mCachedBookmarks.remove(address);
    }





    @Override
    public void addHistory(WebPage webPage) {
        mRemoteDataSource.addHistory(webPage);
        mLocalDataSource.addHistory(webPage);

        if (mCachedHistory == null) {
            mCachedHistory = new LinkedHashMap<>();
        }
        mCachedHistory.put(webPage.hashCode()+"", webPage);
    }

    @Override
    public void deleteAllHistory() {
        mLocalDataSource.deleteAllHistory();

        if (mCachedHistory == null) {
            mCachedHistory = new LinkedHashMap<>();
        }
        mCachedHistory.clear();
    }

    @Override
    public void removeHistory(WebPage webPage) {
        mRemoteDataSource.removeHistory(webPage);
        mLocalDataSource.removeHistory(webPage);
        mCachedHistory.remove(webPage.hashCode()+"");
    }

    @Override
    public void refreshHistory() {
        mHistoryCacheIsDirty=true;
    }

    @Override
    public void getHistory(@NonNull final LoadCallback callback) {
        if (mCachedHistory != null && !mHistoryCacheIsDirty) {
            callback.onLoaded(new ArrayList<>(mCachedHistory.values()));
            return;
        }

        if (mHistoryCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getHistoryFromRemoteDataSource(callback);
        } else {
            mLocalDataSource.getHistory(new LoadCallback() {
                @Override
                public void onLoaded(List<WebPage> webPages) {
                    refreshHistoryCache(webPages);
                    callback.onLoaded(new ArrayList<>(mCachedHistory.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }

            });
        }
    }


    @Override
    public void getDownload(@NonNull final DownloadLoadCallback callback) {
        if (mCachedDownload != null && !mDownloadCacheIsDirty) {
            callback.onLoaded(new ArrayList<>(mCachedDownload.values()));
            return;
        }

        if (mDownloadCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getDownloadFromRemoteDataSource(callback);
        } else {
            mLocalDataSource.getDownload(new DownloadLoadCallback() {
                @Override
                public void onLoaded(List<Download> downloads) {
                    refreshDownloadCache(downloads);
                    callback.onLoaded(new ArrayList<>(mCachedDownload.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }

            });
        }
    }

    @Override
    public void refreshDownload() {
        mDownloadCacheIsDirty = true;
    }

    @Override
    public void addDownload(Download download) {
        mRemoteDataSource.addDownload(download);
        mLocalDataSource.addDownload(download);

        if (mCachedDownload == null) {
            mCachedDownload = new LinkedHashMap<>();
        }
        mCachedDownload.put(download.url, download);
    }


    @Override
    public void deleteAllDownload() {
        mLocalDataSource.deleteAllDownload();

        if (mCachedDownload == null) {
            mCachedDownload = new LinkedHashMap<>();
        }
        mCachedDownload.clear();
    }

    @Override
    public void removeDownload(Download download) {
        mRemoteDataSource.removeDownload(download);
        mLocalDataSource.removeDownload(download);
        mCachedDownload.remove(download);
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
    public void startAll() {

    }

    @Override
    public void pauseAll() {

    }







    private void refreshHistoryCache(List<WebPage> webPages) {
        if (mCachedHistory == null) {
            mCachedHistory = new LinkedHashMap<>();
        }
        mCachedHistory.clear();
        for (WebPage webPage : webPages) {
            mCachedHistory.put(webPage.hashCode()+"", webPage);
        }
        mHistoryCacheIsDirty = false;
    }

    private void refreshBookmarksCache(List<WebPage> webPages) {
        if (mCachedBookmarks == null) {
            mCachedBookmarks = new LinkedHashMap<>();
        }
        mCachedBookmarks.clear();
        for (WebPage webPage : webPages) {
            mCachedBookmarks.put(webPage.getUrl(), webPage);
        }
        mBookmarksCacheIsDirty = false;
    }

    private void refreshDownloadCache(List<Download> downloads) {
        if (mCachedDownload == null) {
            mCachedDownload = new LinkedHashMap<>();
        }
        mCachedDownload.clear();
        for (Download download : downloads) {
            mCachedDownload.put(download.url, download);
        }
        mDownloadCacheIsDirty = false;
    }


    private void refreshBookmarksLocalDataSource(List<WebPage> webPages) {
        mLocalDataSource.deleteAllBookmarks();
        for (WebPage webPage : webPages) {
            mLocalDataSource.addBookmarks(webPage);
        }
    }
    private void getBookmarksFromRemoteDataSource(@NonNull final LoadCallback callback) {
        mRemoteDataSource.getBookmarks(new LoadCallback() {
            @Override
            public void onLoaded(List<WebPage> webPages) {
                refreshBookmarksCache(webPages);
                refreshBookmarksLocalDataSource(webPages);
                callback.onLoaded(new ArrayList<>(mCachedBookmarks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshHistoryLocalDataSource(List<WebPage> webPages) {
        mLocalDataSource.deleteAllHistory();
        for (WebPage webPage : webPages) {
            mLocalDataSource.addHistory(webPage);
        }
    }
    private void getHistoryFromRemoteDataSource(@NonNull final LoadCallback callback) {
        mRemoteDataSource.getHistory(new LoadCallback() {
            @Override
            public void onLoaded(List<WebPage> webPages) {
                refreshHistoryCache(webPages);
                refreshHistoryLocalDataSource(webPages);
                callback.onLoaded(new ArrayList<>(mCachedHistory.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshDownloadLocalDataSource(List<Download> downloads) {
        mLocalDataSource.deleteAllDownload();
        for (Download download : downloads) {
            mLocalDataSource.addDownload(download);
        }
    }
    private void getDownloadFromRemoteDataSource(@NonNull final DownloadLoadCallback callback) {
        mRemoteDataSource.getDownload(new DownloadLoadCallback() {
            @Override
            public void onLoaded(List<Download> downloads) {
                refreshDownloadCache(downloads);
                refreshDownloadLocalDataSource(downloads);
                callback.onLoaded(new ArrayList<>(mCachedDownload.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}

