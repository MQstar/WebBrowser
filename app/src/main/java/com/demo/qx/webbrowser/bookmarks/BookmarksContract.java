package com.demo.qx.webbrowser.bookmarks;

import com.demo.qx.webbrowser.BasePresenter;
import com.demo.qx.webbrowser.BaseView;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.List;

/**
 * Created by qx on 16/10/26.
 */
//这个没必要看.无视之.
public interface BookmarksContract {
    interface View extends BaseView<Presenter> {
        void showBookmarks(List<WebPage> webPageList);

        void showNoBookmarks();
    }

    interface Presenter extends BasePresenter {
        void addBookmarks(WebPage webPage);

        void loadBookmarks(boolean forceUpdate);

        void removeBookmarks(String address);
    }
}
