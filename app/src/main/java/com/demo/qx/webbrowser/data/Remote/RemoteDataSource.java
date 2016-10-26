package com.demo.qx.webbrowser.data.Remote;

import com.demo.qx.webbrowser.data.DataSource;

/**
 * Created by qx on 16/10/25.
 */
public class RemoteDataSource implements DataSource {
    private static RemoteDataSource INSTANCE;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }
}
