package com.demo.qx.webbrowser.data;

import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.Local.LocalDataSource;
import com.demo.qx.webbrowser.data.Remote.RemoteDataSource;

/**
 * Created by qx on 16/10/25.
 */

public class Repository implements DataSource {
    private static Repository INSTANCE = null;

    private final DataSource mTasksRemoteDataSource;
    private final DataSource mTasksLocalDataSource;

    private Repository(@NonNull DataSource remoteDataSource,
                       @NonNull DataSource localDataSource) {
        mTasksRemoteDataSource = remoteDataSource;
        mTasksLocalDataSource = localDataSource;
    }

    public static Repository getInstance(RemoteDataSource remoteDataSource, LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }
}

