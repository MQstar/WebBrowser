package com.demo.qx.webbrowser.data.Local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.DataSource;

/**
 * Created by qx on 16/10/25.
 */
public class LocalDataSource implements DataSource {
    private static LocalDataSource INSTANCE;
    private DBHelp mDBHelp;

    private LocalDataSource(@NonNull Context context) {
        mDBHelp = new DBHelp(context);
    }

    public static LocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }
}
