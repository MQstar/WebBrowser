package com.demo.qx.webbrowser.bookmarks;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.DataSource;
import com.demo.qx.webbrowser.data.Repository;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.List;

/**
 * Created by qx on 16/10/26.
 */

public class BookmarksPresenter implements BookmarksContract.Presenter {

    private final Repository mRepository;
    private final BookmarksContract.View mView;
    private boolean mFirstLoad = true;

    public BookmarksPresenter(@NonNull Repository repository, @NonNull BookmarksFragment bookmarksFragment) {
        mRepository = repository;
        mView = bookmarksFragment;
        mView.setPresenter(this);
    }

    @Override
    public void addBookmarks(WebPage webPage) {
        mRepository.addBookmarks(webPage);
    }

    @Override
    public void start() {
        loadBookmarks(false || mFirstLoad);
    }

    @Override
    public void loadBookmarks(boolean forceUpdate) {

        mFirstLoad = false;
        if (forceUpdate) {
            mRepository.refreshBookmarks();
        }

        mRepository.getBookmarks(new DataSource.LoadCallback() {
            @Override
            public void onLoaded(List<WebPage> webPages) {
                processBookmarks(webPages);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNoBookmarks();
            }

        });
    }

    @Override
    public void removeBookmarks(String address) {
        mRepository.removeBookmarks(address);
        loadBookmarks(false);
    }

    private void processBookmarks(List<WebPage> webPages) {
        if (webPages.isEmpty()) {
            mView.showNoBookmarks();
        } else {
            mView.showBookmarks(webPages);
        }
    }
}