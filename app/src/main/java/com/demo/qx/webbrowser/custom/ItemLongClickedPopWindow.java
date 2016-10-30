package com.demo.qx.webbrowser.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.demo.qx.webbrowser.R;

/**
 * Created by qx on 16/10/30.
 */
public class ItemLongClickedPopWindow extends PopupWindow{
    public static final int BOOKMARKS_POPUPWINDOW = 0;
    public static final int HISTORY_POPUPWINDOW = 1;
    public static final int SRC_IMAGE_ANCHOR_TYPE_POPUPWINDOW = 2;
    public static final int SRC_ANCHOR_TYPE_POPUPWINDOW = 3;

    private LayoutInflater itemLongClickedPopWindowInflater;
    private View itemLongClickedPopWindowView;
    private Context mContext;

    private int type;

    /**
     * 构造函数
     * @param context 上下文
     * @param width 宽度
     * @param height 高度
     * */
    public ItemLongClickedPopWindow(Context context, int type, int width, int height){
        super(context);
        mContext = context;
        this.type = type;

        initTab();
        //设置默认选项
        setWidth(width);
        setHeight(height);
        setContentView(this.itemLongClickedPopWindowView);
        setBackgroundDrawable(mContext.getDrawable(R.drawable.popup_size));
        setOutsideTouchable(true);
        setFocusable(true);
    }


    //实例化
    private void initTab(){
        this.itemLongClickedPopWindowInflater = LayoutInflater.from(mContext);
        switch(type){
            case BOOKMARKS_POPUPWINDOW:
                this.itemLongClickedPopWindowView = this.itemLongClickedPopWindowInflater.inflate(R.layout.popup_bookmarks, null);
                break;
            case HISTORY_POPUPWINDOW:
                this.itemLongClickedPopWindowView = this.itemLongClickedPopWindowInflater.inflate(R.layout.popup_history, null);
                break;
            case SRC_ANCHOR_TYPE_POPUPWINDOW:
                this.itemLongClickedPopWindowView = this.itemLongClickedPopWindowInflater.inflate(R.layout.popup_anchor, null);
                break;
            case SRC_IMAGE_ANCHOR_TYPE_POPUPWINDOW:
                this.itemLongClickedPopWindowView = this.itemLongClickedPopWindowInflater.inflate(R.layout.popup_anchor, null);
                break;
        }

    }

    public View getView(int id){
        return this.itemLongClickedPopWindowView.findViewById(id);
    }
}
