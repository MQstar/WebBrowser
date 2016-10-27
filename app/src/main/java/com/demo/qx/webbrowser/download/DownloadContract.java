package com.demo.qx.webbrowser.download;

import com.demo.qx.webbrowser.BasePresenter;
import com.demo.qx.webbrowser.BaseView;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.List;

/**
 * Created by qx on 16/10/26.
 */

public interface DownloadContract {
    interface View extends BaseView<Presenter> {
        void showDownload(List<WebPage> webPages);

        void showNoDownload();
    }

    interface Presenter extends BasePresenter {

        List<WebPage> getDownload();
    }
}
