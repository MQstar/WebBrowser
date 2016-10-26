package com.demo.qx.webbrowser.bookmarks;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Repository;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.List;

/**
 * Created by qx on 16/10/26.
 */

public class BookmarksPresenter implements BookmarksContract.Presenter{

    private final Repository mRepository;
    private final BookmarksContract.View mView;
    public BookmarksPresenter(@NonNull Repository repository, @NonNull BookmarksFragment bookmarksFragment) {
        mRepository = repository;
        mView = bookmarksFragment;
        mView.setPresenter(this);
    }
    @Override
    public void start() {

    }
    @Override
    public void addBookmarks(WebPage webPage){
        mRepository.addBookmarks(webPage);
    }
    @Override
    public void showBookmarks(List<WebPage> webPageList){
        mView.showBookmarks(webPageList);
    }

    @Override
    public List<WebPage> getBookmarks() {
        return null;
    }
}
