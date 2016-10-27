package com.demo.qx.webbrowser.history;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Repository;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.List;

/**
 * Created by qx on 16/10/26.
 */

public class HistoryPresenter implements HistoryContract.Presenter{
    private final Repository mRepository;
    private final HistoryContract.View mView;
    public HistoryPresenter(@NonNull Repository repository, @NonNull HistoryFragment historyFragment) {
        mRepository = repository;
        mView = historyFragment;
        mView.setPresenter(this);
    }
    @Override
    public void start() {

    }
    @Override
    public void addHistory(WebPage webPage){
        mRepository.addHistory(webPage);
    }
    @Override
    public void showHistory(List<WebPage> webPageList){
        mView.showHistory(webPageList);
    }

    @Override
    public List<WebPage> getHistory() {
        return null;
    }
}
