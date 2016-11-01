package com.demo.qx.webbrowser.download;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Download;
import com.demo.qx.webbrowser.data.source.DataSource;
import com.demo.qx.webbrowser.data.source.Repository;
import com.demo.qx.webbrowser.downloadUnity.DownloadManager;

import java.util.List;

/**
 * Created by qx on 16/10/26.
 */

public class DownloadPresenter implements DownloadContract.Presenter{
    private final Repository mRepository;
    private final DownloadContract.View mView;
    private boolean mFirstLoad = true;
    public DownloadPresenter(@NonNull Repository repository, @NonNull DownloadFragment downloadFragment) {
        mRepository = repository;
        mView = downloadFragment;
        mView.setPresenter(this);
    }
    @Override
    public void start() {
        loadDownload(false || mFirstLoad);
    }
    @Override
    public void loadDownload(boolean forceUpdate) {

        mFirstLoad = false;
        if (forceUpdate) {
            mRepository.refreshDownload();
        }

        mRepository.getDownload(new DataSource.DownloadLoadCallback() {
            @Override
            public void onLoaded(List<Download> downloads) {
                processDownload(downloads);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNoDownload();
            }

        });
    }

    @Override
    public void pause(Download download, DownloadManager downloadManager) {
        mRepository.pause(download,downloadManager);
    }

    @Override
    public void resume(Download download, DownloadManager downloadManager) {
        mRepository.resume(download,downloadManager);
    }

    @Override
    public void removeDownloadAndFile(Download download) {
        mRepository.removeDownloadAndFile(download);
        loadDownload(false);
    }

    @Override
    public void removeDownload(Download download) {
        mRepository.removeDownload(download);
        loadDownload(false);
    }

    @Override
    public void startAll(DownloadManager downloadManager) {
        mRepository.startAll(downloadManager);
    }

    @Override
    public void pauseAll(DownloadManager downloadManager) {
        mRepository.pauseAll(downloadManager);
    }
    private void processDownload(List<Download> downloads) {
        if (downloads.isEmpty()) {
            mView.showNoDownload();
        } else {
            mView.showDownload(downloads);
        }
    }
}

