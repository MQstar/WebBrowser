package com.demo.qx.webbrowser.bookmarks;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.demo.qx.webbrowser.R;
import com.demo.qx.webbrowser.utils.ActivityUtils;
import com.demo.qx.webbrowser.utils.Injection;

public class BookmarksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        //寻找并设置Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setLogo(R.drawable.ic_star_border_black_24dp);
        ab.setTitle("");
        ab.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        //寻找fragment
        BookmarksFragment bookmarksFragment = (BookmarksFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (bookmarksFragment == null) {
            //实例化fragment
            bookmarksFragment = BookmarksFragment.newInstance();
            //将fragment添加到activity
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    bookmarksFragment, R.id.contentFrame);
        }
        //实例化presenter
        new BookmarksPresenter(
                Injection.provideTasksRepository(getApplicationContext()), bookmarksFragment);
    }
    //toolbar上的菜单的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //回退
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
