package com.demo.qx.webbrowser.download;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Repository;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.List;

/**
 * Created by qx on 16/10/26.
 */

public class DownloadPresenter implements DownloadContract.Presenter{
    private final Repository mRepository;
    private final DownloadContract.View mView;
    public DownloadPresenter(@NonNull Repository repository, @NonNull DownloadFragment downloadFragment) {
        mRepository = repository;
        mView = downloadFragment;
        mView.setPresenter(this);
    }
    @Override
    public void start() {

    }

    @Override
    public List<WebPage> getDownload() {
        return null;
    }
}

