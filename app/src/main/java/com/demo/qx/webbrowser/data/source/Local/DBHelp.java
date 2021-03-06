package com.demo.qx.webbrowser.data.source.Local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qx on 16/10/25.
 */

public class DBHelp extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "WebBrowser.db";

    private static final String TEXT_TYPE = " TEXT";

    public static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_BOOKMARKS =
            "CREATE TABLE IF NOT EXISTS " + PersistenceContract.Bookmarks.TABLE_NAME + " (" +
                    PersistenceContract.Bookmarks._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.Bookmarks.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.Bookmarks.COLUMN_NAME_ADDRESS + TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_HISTORY =
            "CREATE TABLE IF NOT EXISTS " + PersistenceContract.History.TABLE_NAME + " (" +
                    PersistenceContract.History._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.History.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.History.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.History.COLUMN_NAME_DATE + TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_DOWNLOAD =
            "CREATE TABLE IF NOT EXISTS " + PersistenceContract.Download.TABLE_NAME + " (" +
                    PersistenceContract.Download._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.Download.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.Download.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.Download.COLUMN_NAME_SIZE + INTEGER_TYPE +
                    PersistenceContract.Download.COLUMN_NAME_CURRENT + INTEGER_TYPE + COMMA_SEP +
                    " )";

    public DBHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BOOKMARKS);
        db.execSQL(SQL_CREATE_HISTORY);
        db.execSQL(SQL_CREATE_DOWNLOAD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
