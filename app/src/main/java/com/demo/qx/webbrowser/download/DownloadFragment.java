package com.demo.qx.webbrowser.download;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.qx.webbrowser.R;
import com.demo.qx.webbrowser.custom.ItemLongClickedPopWindow;
import com.demo.qx.webbrowser.data.Download;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qx on 16/10/26.
 */

public class DownloadFragment extends Fragment implements DownloadContract.View {
    private DownloadContract.Presenter mPresenter;
    private DownloadAdapter mListAdapter;
    private ListView mListView;
    private TextView mTextView;
    private ItemLongClickedPopWindow mPopupWindow;

    /*private DataWatcher mWatcher=new DataWatcher() {
        @Override
        public void notifyUpdate(Download arg) {
           *//*int index=mDownloads.indexOf(arg);
            if (index==-1)
            mDownloads.add(arg);
            else {
                mDownloads.remove(index);
                mDownloads.add(index,arg);
            }
            mListAdapter.notifyDataSetChanged()*//*;
        }
    };*/
    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }

    @Override
    public void setPresenter(DownloadContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new DownloadAdapter(new ArrayList<Download>(0), mItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_download, container, false);
        mListView = (ListView) root.findViewById(R.id.download_list);
        mTextView = (TextView) root.findViewById(R.id.no_download);
        mListView.setAdapter(mListAdapter);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start_all:
                mPresenter.startAll();
                break;
            case R.id.pause_all:
                mPresenter.pauseAll();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.download_menu, menu);
    }

    @Override
    public void showDownload(List<Download> downloads) {
        mListAdapter.replaceData(downloads);

        mListView.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);
    }

    @Override
    public void showNoDownload() {
        mListView.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
    }

    private class ItemClickedListener implements View.OnClickListener {
        private Download mDownload;

        public ItemClickedListener(Download download) {
            mDownload = download;
        }

        @Override
        public void onClick(View view) {
            mPopupWindow.dismiss();
            if (view.getId() == R.id.item_long_click_delete_task) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("删除下载任务")
                        .setMessage("是否要删除\"" + mDownload.name + "\"这个下载任务？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.removeDownload(mDownload);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
            } else if (view.getId() == R.id.item_long_click_delete_task_and_file) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("删除下载任务及文件")
                        .setMessage("是否要删除\"" + mDownload.name + "\"这个下载任务？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.removeDownloadAndFile(mDownload);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
            }
        }
    }

    private class DownloadAdapter extends BaseAdapter {

        private List<Download> mDownloads;
        private ItemListener mItemListener;

        public DownloadAdapter(List<Download> downloads, ItemListener itemListener) {
            setList(downloads);
            mItemListener = itemListener;
        }

        public void replaceData(List<Download> downloads) {
            setList(downloads);
            notifyDataSetChanged();
        }

        private void setList(List<Download> downloads) {
            mDownloads = downloads;
        }

        @Override
        public int getCount() {
            return mDownloads.size();
        }

        @Override
        public Download getItem(int i) {
            return mDownloads.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.item_download, viewGroup, false);
            }

            final Download download = getItem(i);

            TextView titleTV = (TextView) rowView.findViewById(R.id.download_name);
            ProgressBar progressBar = (ProgressBar) rowView.findViewById(R.id.download_progress);
            titleTV.setText(download.name);
            progressBar.setProgress((int) ((download.currentLength / download.contentLength) * 100));
            rowView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mPopupWindow = new ItemLongClickedPopWindow(getActivity(), ItemLongClickedPopWindow.DOWNLOAD_POPUPWINDOW, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mPopupWindow.showAsDropDown(v, v.getWidth() / 2, -v.getHeight() / 2);
                    mPopupWindow.getView(R.id.item_long_click_delete).setOnClickListener(new ItemClickedListener(download));
                    return true;
                }
            });
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onClick(download);
                }
            });

            return rowView;
        }
    }
    ItemListener mItemListener = new ItemListener() {
        @Override
        public void onClick(Download clickedDownload) {
            if (clickedDownload.status == Download.DownloadStatus.paused)
                mPresenter.resume(clickedDownload);
            else
                mPresenter.pause(clickedDownload);
        }
    };

    public interface ItemListener {

        void onClick(Download clickedDownload);

    }
}