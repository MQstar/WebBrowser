package com.demo.qx.webbrowser.history;

import android.app.Activity;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.demo.qx.webbrowser.R;
import com.demo.qx.webbrowser.data.WebPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qx on 16/10/26.
 */

public class HistoryFragment extends Fragment implements HistoryContract.View {
    private HistoryContract.Presenter mPresenter;
    private HistoryAdapter mListAdapter;
    private ExpandableListView mListView;
    private TextView mTextView;
    private PopupWindow mPopupWindow;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public void setPresenter(HistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new HistoryAdapter(getActivity(), new ArrayList<String>(), new ArrayList<List<WebPage>>(), mItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        mListView = (ExpandableListView) root.findViewById(R.id.history_list);
        mListView.setAdapter(mListAdapter);
        mTextView = (TextView) root.findViewById(R.id.no_history);
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
            case R.id.clear_all:
                mPresenter.removeAll();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history_fragment_menu, menu);
    }

    @Override
    public void showHistory(List<WebPage> webPages) {
        List<String> dateList = new ArrayList<>();
        List<List<WebPage>> dateWebList = new ArrayList<>();
        Map<String, List<WebPage>> sort = new HashMap<>();
        for (WebPage webPage : webPages) {
            String date = webPage.getDate();
            if (!dateList.contains(date)) {
                dateList.add(date);
                List<WebPage> temp = new ArrayList<>();
                temp.add(webPage);
                sort.put(date, temp);
            } else {
                sort.get(date).add(webPage);
            }
        }
        Collections.sort(dateList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        List<Map.Entry<String, List<WebPage>>> sortedList = new ArrayList<>(sort.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<String, List<WebPage>>>() {
            @Override
            public int compare(Map.Entry<String, List<WebPage>> o1, Map.Entry<String, List<WebPage>> o2) {
                return o2.getKey().compareTo(o1.getKey());
            }
        });
        for (Map.Entry<String, List<WebPage>> entry : sortedList) {
            dateWebList.add(entry.getValue());
        }

        mListAdapter.replaceData(dateList, dateWebList);
        mListView.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);
    }

    @Override
    public void showNoHistory() {
        mListView.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
    }


    private class ItemClickedListener implements View.OnClickListener {
        private WebPage mWebPage;

        public ItemClickedListener(WebPage webPage) {
            mWebPage = webPage;
        }

        @Override
        public void onClick(View view) {
            mPopupWindow.dismiss();
            if (view.getId() == R.id.item_longclicked_deleteHistory) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("删除历史")
                        .setMessage("是否要删除\"" + mWebPage.getTitle() + "\"这个历史？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.removeHistory(mWebPage);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
            }

        }

    }

    ItemListener mItemListener = new ItemListener() {
        @Override
        public void onClick(WebPage clickedWebPage) {
            ((HistoryActivity) getActivity()).openURL(clickedWebPage.getUrl());
        }
    };

    public interface ItemListener {
        void onClick(WebPage clickedWebPage);

    }

    private class HistoryAdapter extends BaseExpandableListAdapter {
        private List<String> mGroupData;
        private List<List<WebPage>> mChildData;
        private Activity mActivity;
        private ItemListener mItemListener;

        public HistoryAdapter(Activity activity, List<String> groupData, List<List<WebPage>> childData, ItemListener itemListener) {
            mActivity = activity;
            mChildData = childData;
            mGroupData = groupData;
            mItemListener = itemListener;
        }

        public void replaceData(List<String> groupData, List<List<WebPage>> childData) {
            mGroupData = groupData;
            mChildData = childData;
            notifyDataSetChanged();
        }

        //获取父元素
        public String getGroup(int groupPosition) {
            return mGroupData.get(groupPosition);
        }

        //获取父元素的位置
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //获取父元素的个数
        public int getGroupCount() {
            return mGroupData.size();
        }

        //获取子元素
        public WebPage getChild(int groupPosition, int childPosition) {
            return mChildData.get(groupPosition).get(childPosition);
        }

        //获取子元素的位置
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        //获取子元素的个数
        public int getChildrenCount(int groupPosition) {
            return mChildData.get(groupPosition).size();
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = mActivity.getLayoutInflater().inflate(R.layout.item_webpage, parent, false);
            }
            final WebPage webPage = mChildData.get(groupPosition).get(childPosition);
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView address = (TextView) v.findViewById(R.id.address);
            title.setText(mChildData.get(groupPosition).get(childPosition).getTitle());
            address.setText(mChildData.get(groupPosition).get(childPosition).getUrl());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onClick(webPage);
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_history, null);
                    mPopupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_size));
                    mPopupWindow.setOutsideTouchable(true);
                    mPopupWindow.showAsDropDown(v, v.getWidth() / 2, -v.getHeight() / 2);
                    contentView.findViewById(R.id.item_longclicked_deleteHistory).setOnClickListener(new ItemClickedListener(webPage));
                    return true;
                }
            });
            return v;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            View v = convertView;
            if (convertView == null) {
                v = mActivity.getLayoutInflater().inflate(R.layout.item_date, parent, false);
            }
            TextView date = (TextView) v.findViewById(R.id.date);
            date.setText(mGroupData.get(groupPosition));
            return v;
        }
    }
}

