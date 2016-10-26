package com.demo.qx.webbrowser;

/**
 * Created by qx on 16/10/5.
 */

public interface MainContract {
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
