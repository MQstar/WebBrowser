package com.demo.qx.webbrowser.download.downloadUnity;

import com.demo.qx.webbrowser.data.Download;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by qx on 16/10/31.
 */

public class DataChanger extends Observable {
    private static DataChanger mInstance;
    private LinkedHashMap<Integer,Download> mLinkedHashMap;
    private DataChanger() {

    }
    public static synchronized DataChanger getInstance(){
        if (mInstance==null){
            mInstance=new DataChanger();
        }
        return mInstance;
    }

    public void postStatus(Download download){
        mLinkedHashMap.put(download.hashCode(),download);
    setChanged();
    notifyObservers(download);
}

    public ArrayList<Download> queryAllRecoverableDownload(){
        ArrayList<Download> arrayList=null;
        for (Map.Entry<Integer, Download> integerDownloadEntry : mLinkedHashMap.entrySet()) {
            if (integerDownloadEntry.getValue().status== Download.DownloadStatus.paused){
                if (arrayList==null)
                    arrayList=new ArrayList<>();
                arrayList.add(integerDownloadEntry.getValue());
            }
        }
        return arrayList;
    }
}
