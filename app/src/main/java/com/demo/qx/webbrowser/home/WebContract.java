package com.demo.qx.webbrowser.home;

import com.demo.qx.webbrowser.BasePresenter;
import com.demo.qx.webbrowser.BaseView;

/**
 * Created by qx on 16/10/5.
 */

public interface WebContract {
    interface View extends BaseView<Presenter> {
        void setTitle(String title);

        void setAddress(String url);

        void changeProgress(int progress);
    }

    interface Presenter extends BasePresenter {

        void setTitle(String title);

        void setAddress(String url);

        void changeProgress(int progress);
    }
}
