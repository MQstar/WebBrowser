package com.demo.qx.webbrowser.history;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.source.DataSource;
import com.demo.qx.webbrowser.data.source.Repository;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.List;

/**
 * Created by qx on 16/10/26.
 */

public class HistoryPresenter implements HistoryContract.Presenter{
    private final Repository mRepository;
    private final HistoryContract.View mView;
    private boolean mFirstLoad = true;
    public HistoryPresenter(@NonNull Repository repository, @NonNull HistoryFragment historyFragment) {
        mRepository = repository;
        mView = historyFragment;
        mView.setPresenter(this);
    }
    @Override
    public void start() {
        loadHistory(false || mFirstLoad);
    }


    @Override
    public void removeAll() {
        mRepository.deleteAllHistory();
        loadHistory(false);
    }

    @Override
    public void loadHistory(boolean forceUpdate) {
        mFirstLoad = false;
        if (forceUpdate) {
            mRepository.refreshHistory();
        }

        mRepository.getHistory(new DataSource.LoadCallback() {
            @Override
            public void onLoaded(List<WebPage> webPages) {
                processHistory(webPages);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNoHistory();
            }

        });
    }

    @Override
    public void removeHistory(WebPage webPage) {
        mRepository.removeHistory(webPage);
        loadHistory(false);
    }
    private void processHistory(List<WebPage> webPages) {
        if (webPages.isEmpty()) {
            mView.showNoHistory();
        } else {
            mView.showHistory(webPages);
        }
    }
}
