package com.demo.qx.webbrowser.download;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.demo.qx.webbrowser.R;
import com.demo.qx.webbrowser.utils.ActivityUtils;
import com.demo.qx.webbrowser.utils.Injection;

public class DownloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            ab.setTitle("下载");
            ab.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
            ab.setDisplayHomeAsUpEnabled(true);



            DownloadFragment downloadFragment = (DownloadFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.contentFrame);
            if (downloadFragment == null) {
                downloadFragment = DownloadFragment.newInstance();
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        downloadFragment, R.id.contentFrame);
            }

            new DownloadPresenter(
                    Injection.provideTasksRepository(getApplicationContext()), downloadFragment);
        }
    void openURL(String url){
        Intent intent=getIntent();
        intent.putExtra("URL",url);
        setIntent(intent);
    }
}
