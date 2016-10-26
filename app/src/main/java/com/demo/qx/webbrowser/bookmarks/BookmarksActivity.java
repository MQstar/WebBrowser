package com.demo.qx.webbrowser.bookmarks;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.demo.qx.webbrowser.R;
import com.demo.qx.webbrowser.utils.ActivityUtils;
import com.demo.qx.webbrowser.utils.Injection;

public class BookmarksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("书签");
        ab.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_36dp);
        ab.setDisplayHomeAsUpEnabled(true);



        BookmarksFragment bookmarksFragment = (BookmarksFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (bookmarksFragment == null) {
            bookmarksFragment = BookmarksFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    bookmarksFragment, R.id.contentFrame);
        }

        new BookmarksPresenter(
                Injection.provideTasksRepository(getApplicationContext()), bookmarksFragment);
    }
}
