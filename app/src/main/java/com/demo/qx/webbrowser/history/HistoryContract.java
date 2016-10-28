package com.demo.qx.webbrowser.history;

import com.demo.qx.webbrowser.BasePresenter;
import com.demo.qx.webbrowser.BaseView;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.List;

/**
 * Created by qx on 16/10/26.
 */

public interface HistoryContract {
    interface View extends BaseView<Presenter> {

        void showHistory(List<WebPage> webPages);

        void showNoHistory();
    }

    interface Presenter extends BasePresenter {
        void removeAll();

        void loadHistory(boolean forceUpdate);

        void removeHistory(WebPage webPage);
    }
}
