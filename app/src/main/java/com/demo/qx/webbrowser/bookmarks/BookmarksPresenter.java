package com.demo.qx.webbrowser.bookmarks;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.source.DataSource;
import com.demo.qx.webbrowser.data.source.Repository;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.List;

/**
 * Created by qx on 16/10/26.
 */

public class BookmarksPresenter implements BookmarksContract.Presenter {

    private final Repository mRepository;
    private final BookmarksContract.View mView;
    private boolean mFirstLoad = true;

    //构造方法,获得了v层(fragment)和m层(repository)
    public BookmarksPresenter(@NonNull Repository repository, @NonNull BookmarksFragment bookmarksFragment) {
        mRepository = repository;
        mView = bookmarksFragment;
        mView.setPresenter(this);
    }

    //顾名思义
    @Override
    public void addBookmarks(WebPage webPage) {
        mRepository.addBookmarks(webPage);
    }

    //加载书签,参数是用来判断是不是第一次加载(可以提升以后的加载速度)
    @Override
    public void start() {
        loadBookmarks(false || mFirstLoad);
    }

    //真正的书签加载
    @Override
    public void loadBookmarks(boolean forceUpdate) {

        //把是不是第一次的标准位置为false
        mFirstLoad = false;
        //这个只有在第一次加载的时候才会执行,也就是要从网络加载数据
        if (forceUpdate) {
            mRepository.refreshBookmarks();
        }

        //从本地数据库获得数据,并在获得以后回调以下方法
        mRepository.getBookmarks(new DataSource.LoadCallback() {
            //加载成功时被回调的方法
            @Override
            public void onLoaded(List<WebPage> webPages) {
                processBookmarks(webPages);
            }

            //加载失败时被回调的方法
            @Override
            public void onDataNotAvailable() {
                mView.showNoBookmarks();
            }

        });
    }

    //删除书签,并更新列表
    @Override
    public void removeBookmarks(String address) {
        mRepository.removeBookmarks(address);
        loadBookmarks(false);
    }

    //就是这个方法把数据传给view的!!!!!
    private void processBookmarks(List<WebPage> webPages) {
        if (webPages.isEmpty()) {
            mView.showNoBookmarks();
        } else {
            mView.showBookmarks(webPages);
        }
    }
}