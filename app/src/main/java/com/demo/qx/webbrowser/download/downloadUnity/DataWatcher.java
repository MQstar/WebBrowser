package com.demo.qx.webbrowser.download.downloadUnity;

import com.demo.qx.webbrowser.data.Download;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by qx on 16/10/31.
 */

public abstract class DataWatcher implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Download){
            notifyUpdate((Download)arg);
        }
    }

    public abstract void notifyUpdate(Download arg);
}
