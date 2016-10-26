package com.demo.qx.webbrowser.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by qx on 16/10/26.
 */

public class WebPage {

    @NonNull
    private final String mUrl;

    @Nullable
    private final String mTitle;

    @Nullable
    private final String mDate;

    public WebPage(@NonNull String url) {
        this(url,url);
    }
    public WebPage(@NonNull String url, String title) {
        this(url,title,null);
    }
    public WebPage(@NonNull String url, String title, String date) {
        mUrl = url;
        mTitle = title;
        mDate = date;
    }

    @Nullable
    public String getDate() {
        return mDate;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String getUrl() {
        return mUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebPage webPage = (WebPage) o;

        if (!mUrl.equals(webPage.mUrl)) return false;
        return mDate != null ? mDate.equals(webPage.mDate) : webPage.mDate == null;

    }

    @Override
    public int hashCode() {
        int result = mUrl.hashCode();
        result = 31 * result + (mDate != null ? mDate.hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Page with title " + mTitle;
    }
}
