package com.demo.qx.webbrowser.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.demo.qx.webbrowser.R;
import com.demo.qx.webbrowser.utils.ActivityUtils;
import com.demo.qx.webbrowser.utils.Injection;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("历史");
        ab.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        HistoryFragment historyFragment = (HistoryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (historyFragment == null) {
            historyFragment = HistoryFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    historyFragment, R.id.contentFrame);
        }

        new HistoryPresenter(
                Injection.provideTasksRepository(getApplicationContext()), historyFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void openURL(String url) {
        Intent intent = getIntent();
        intent.putExtra("URL", url);
        setResult(RESULT_OK, intent);
        finish();
    }
}

