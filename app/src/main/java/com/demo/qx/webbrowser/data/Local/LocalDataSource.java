package com.demo.qx.webbrowser.data.Local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.demo.qx.webbrowser.data.DataSource;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qx on 16/10/25.
 */
public class LocalDataSource implements DataSource {
    private static LocalDataSource INSTANCE;
    private DBHelp mDBHelper;

    private LocalDataSource(@NonNull Context context) {
        mDBHelper = new DBHelp(context);
    }

    public static LocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }
    @Override
    public void getBookmarks(@NonNull LoadCallback callback) {
        List<WebPage> webPages = new ArrayList<>();
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        String[] projection = {
                PersistenceContract.Bookmarks.COLUMN_NAME_ADDRESS,
                PersistenceContract.Bookmarks.COLUMN_NAME_TITLE
        };

        Cursor c = db.query(
                PersistenceContract.Bookmarks.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String address = c.getString(c.getColumnIndexOrThrow(PersistenceContract.Bookmarks.COLUMN_NAME_ADDRESS));
                String title = c.getString(c.getColumnIndexOrThrow(PersistenceContract.Bookmarks.COLUMN_NAME_TITLE));
                WebPage webPage = new WebPage(address, title);
                webPages.add(webPage);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (webPages.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onLoaded(webPages);
        }

    }



    @Override
    public void deleteAllHistory() {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        db.delete(PersistenceContract.History.TABLE_NAME, null, null);

        db.close();
    }

    @Override
    public void removeBookmarks(String address) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String selection = PersistenceContract.Bookmarks.COLUMN_NAME_ADDRESS + " LIKE ?";
        String[] selectionArgs = { address };

        db.delete(PersistenceContract.Bookmarks.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void removeHistory(@NonNull WebPage webPage) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String selection = PersistenceContract.History.COLUMN_NAME_ADDRESS + " LIKE ?"+" AND "+
                PersistenceContract.History.COLUMN_NAME_DATE + " LIKE ?";
        String[] selectionArgs = { webPage.getUrl(),webPage.getDate() };

        db.delete(PersistenceContract.History.TABLE_NAME, selection, selectionArgs);

        db.close();
    }


    @Override
    public void getHistory(@NonNull LoadCallback callback) {
        List<WebPage> webPages = new ArrayList<>();
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        String[] projection = {
                PersistenceContract.History.COLUMN_NAME_ADDRESS,
                PersistenceContract.History.COLUMN_NAME_TITLE,
                PersistenceContract.History.COLUMN_NAME_DATE
        };

        Cursor c = db.query(
                PersistenceContract.History.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String address = c.getString(c.getColumnIndexOrThrow(PersistenceContract.History.COLUMN_NAME_ADDRESS));
                String title = c.getString(c.getColumnIndexOrThrow(PersistenceContract.History.COLUMN_NAME_TITLE));
                String date = c.getString(c.getColumnIndexOrThrow(PersistenceContract.History.COLUMN_NAME_DATE));
                WebPage webPage = new WebPage(address, title,date);
                webPages.add(webPage);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (webPages.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onLoaded(webPages);
        }
    }


    @Override
    public void addBookmarks(@NonNull WebPage webPage) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PersistenceContract.Bookmarks.COLUMN_NAME_ADDRESS, webPage.getUrl());
        values.put(PersistenceContract.Bookmarks.COLUMN_NAME_TITLE, webPage.getTitle());

        db.insert(PersistenceContract.Bookmarks.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void deleteBookmarks(@NonNull String url) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String selection = PersistenceContract.Bookmarks.COLUMN_NAME_ADDRESS + " LIKE ?";
        String[] selectionArgs = { url };

        db.delete(PersistenceContract.Bookmarks.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void addHistory(WebPage webPage) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PersistenceContract.History.COLUMN_NAME_ADDRESS, webPage.getUrl());
        values.put(PersistenceContract.History.COLUMN_NAME_TITLE, webPage.getTitle());
        values.put(PersistenceContract.History.COLUMN_NAME_DATE, webPage.getDate());
        db.insert(PersistenceContract.History.TABLE_NAME, null, values);

        db.close();
    }
    @Override
    public void refreshBookmarks() {

    }
    @Override
    public void refreshHistory() {

    }
}
