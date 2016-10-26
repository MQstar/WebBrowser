package com.demo.qx.webbrowser;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Repository;

/**
 * Created by qx on 16/10/25.
 */

public class Presenter implements MainContract.Presenter{
    private final Repository mRepository;
    private final MainContract.View mView;
    public Presenter(@NonNull Repository repository, @NonNull WebFragment webFragment) {
        mRepository = repository;
        mView = webFragment;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }


    @Override
    public void setTitle(String title) {
        mView.setTitle(title);
    }

    @Override
    public void setAddress(String url) {
        mView.setAddress(url);
    }

    @Override
    public void changeProgress(int progress) {
        mView.changeProgress(progress);
    }
}
