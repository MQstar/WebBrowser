package com.demo.qx.webbrowser.download;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Download;
import com.demo.qx.webbrowser.data.source.DataSource;
import com.demo.qx.webbrowser.data.source.Repository;

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
    public void pause(Download download) {
        mRepository.pause(download);
    }

    @Override
    public void resume(Download download) {
        mRepository.resume(download);
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
    public void startAll() {
        mRepository.startAll();
    }

    @Override
    public void pauseAll() {
        mRepository.pauseAll();
    }
    private void processDownload(List<Download> downloads) {
        if (downloads.isEmpty()) {
            mView.showNoDownload();
        } else {
            mView.showDownload(downloads);
        }
    }
}

