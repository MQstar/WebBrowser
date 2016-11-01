package com.demo.qx.webbrowser.home;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.qx.webbrowser.R;
import com.demo.qx.webbrowser.custom.ItemLongClickedPopWindow;
import com.demo.qx.webbrowser.custom.MyAppWebViewClient;
import com.demo.qx.webbrowser.custom.MyWebChromeClient;
import com.demo.qx.webbrowser.custom.MyWebView;
import com.demo.qx.webbrowser.data.Download;
import com.demo.qx.webbrowser.data.WebPage;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * Created by qx on 16/10/24.
 */

public class WebFragment extends Fragment implements WebContract.View, View.OnClickListener, TextView.OnEditorActionListener, View.OnLongClickListener ,View.OnTouchListener{
    boolean isLoading;
    WebContract.Presenter mPresenter;
    MyWebView mWebView;
    EditText mEditText;
    TextView mShowAddress;
    TextView mRefresh;
    TextView mMultiWindow;
    ProgressBar mProgressBar;
    View root;
    android.support.v7.app.ActionBar mActionBar;
    PropertyValuesHolder pvhX;
    PropertyValuesHolder pvhY;
    ObjectAnimator scale;
    public String mCurrentTitle;
    static String URL;
    private WebView.WebViewTransport mTransport;
    public Bitmap mBitmap;
    private long mAId;//唯一标识id
    private float point_x, point_y; //手指按下的位置
    ItemLongClickedPopWindow mPopWindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAId=Long.valueOf(getTimeId());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_web, container, false);
        mMultiWindow=((WebActivity)getActivity()).mMultiWindow;
        mActionBar=((WebActivity)getActivity()).ab;
        mRefresh=((WebActivity)getActivity()).mRefresh;
        mEditText=((WebActivity)getActivity()).mEditText;
        mShowAddress= ((WebActivity)getActivity()).mShowAddress;
        mMultiWindow.setOnClickListener(this);
        mRefresh.setOnClickListener(this);
        mShowAddress.setOnClickListener(this);
        mEditText.setOnEditorActionListener(this);
        mEditText.setSelectAllOnFocus(true);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        mWebView = (MyWebView) root.findViewById(R.id.web_view);
        mWebView.setWebChromeClient(new MyWebChromeClient((WebActivity) getActivity(),mPresenter));
        mWebView.setWebViewClient(new MyAppWebViewClient());
        mWebView.setOnLongClickListener(this);
        mWebView.setOnTouchListener(this);
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                mPresenter.addDownload(new Download(url,contentLength));
                Toast.makeText(getActivity(), "开始下载", Toast.LENGTH_SHORT).show();
            }
        });
        if (mTransport!=null)
        {
            mTransport.setWebView(mWebView);
            mTransport=null;
        }else
            mWebView.loadUrl(URL);
        setHasOptionsMenu(true);
        return root;
    }

    public static WebFragment newInstance(String url) {
        URL=url;
        WebFragment webFragment = new WebFragment();
        return webFragment;
    }

    /**
     * handle back button
     */
    @Override
    public void onResume() {

        super.onResume();

        mWebView.setFocusableInTouchMode(true);
        mWebView.requestFocus();
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if (mWebView.canGoBack())
                    {mWebView.goBack();
                    return true;}
                }
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mBitmap=captureWebView(mWebView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroyDrawingCache();
    }

    @Override
    public void setPresenter(@NonNull WebContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.base_toolbar_menu, menu);
    }

    /*@Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden)
            zoomIn();
    }*/

@Override
public boolean onTouch(View v, MotionEvent event) {
   // if (event.getAction() == MotionEvent.ACTION_DOWN) {
        point_x = event.getX();
        point_y = event.getY();
   // }}
    return false;
}

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        hideEdit();
        if (actionId == EditorInfo.IME_ACTION_GO && !TextUtils.isEmpty(v.getText())) {
            mWebView.loadUrl(URLUtil.guessUrl(v.getText().toString()));
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_refresh:
                if (isLoading)
                    mWebView.stopLoading();
                else
                    mWebView.reload();
                break;
            case R.id.show_address:
                mActionBar.hide();
                mEditText.setText(mWebView.getUrl());
                mEditText.setVisibility(View.VISIBLE);
                mEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, 0);
                break;
            case R.id.mult_window_number:
                //zoomOut();
                ((WebActivity)getActivity()).changeWindow();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (mWebView.canGoBack())
                mWebView.goBack();
                return true;
            case R.id.action_new_web:
                ((WebActivity)getActivity()).getNewWebFragment(null);
                break;
            case R.id.action_forward:
                if (mWebView.canGoForward())
                    mWebView.goForward();
                break;
            case  R.id.collect:
                mPresenter.addBookmarks(new WebPage(mWebView.getUrl(),mWebView.getTitle()));
                Toast.makeText(getActivity(), "已收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bookmarks:
                ((WebActivity)getActivity()).openBookmarks();
                break;
            case R.id.history:
                ((WebActivity)getActivity()).openHistory();
                break;
            case R.id.download:
                ((WebActivity)getActivity()).openDownload();
                break;
            case R.id.exit:
                getActivity().finish();
                System.exit(0);
                break;
            default:
                break;
        }
        return true;
    }

    public void setTitle(String title) {
        mCurrentTitle=title;
    }

    public void setAddress(String address) {
        mShowAddress.setText(address);
    }

    public void hideEdit() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        mActionBar.show();
        mEditText.setVisibility(View.GONE);
    }

    public void changeProgress(int progress) {
        if (progress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE) {
            isLoading = true;
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            mRefresh.setBackgroundResource(R.drawable.ic_close_black_24dp);
            mProgressBar.setProgress(progress);
        }
        if (progress == 100) {
            isLoading = false;
            if (mCurrentTitle!=null)
                mShowAddress.setText(mCurrentTitle);
            mProgressBar.setVisibility(ProgressBar.GONE);
            mRefresh.setBackgroundResource(R.drawable.ic_refresh_black_24dp);
            mBitmap=captureWebView(mWebView);
        }
    }

    public void setTransport(WebView.WebViewTransport transport) {
        mTransport = transport;
    }

    //截图
    private Bitmap captureWebView(WebView webView){
        Picture snapShot = webView.capturePicture();

        Bitmap bmp = null;
        int width = snapShot.getWidth();
        int height = (int) (width *13/9);//默认16:9的设备比例，算出截屏的高

        if (width > 0 && height > 0)
        {
            bmp = Bitmap.createBitmap(width ,height , Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            snapShot.draw(canvas);

        }
        return bmp;
    }
    public void zoomOut(){
        pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.6f);
        pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.6f);
        scale = ObjectAnimator.ofPropertyValuesHolder(mWebView, pvhX, pvhY);
        scale.setDuration(50);
        scale.start();
    }

    public void zoomIn(){
        pvhX = PropertyValuesHolder.ofFloat("scaleX", 0.6f, 1f);
        pvhY = PropertyValuesHolder.ofFloat("scaleY", 0.6f, 1f);
        scale = ObjectAnimator.ofPropertyValuesHolder(mWebView, pvhX, pvhY);
        scale.setDuration(50);
        scale.start();
    }

    @NonNull
    private String getTimeId() {
        String strNum = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) ;
        strNum = strNum.substring(2, strNum.length()) ;
        return strNum;
    }

    public long getAId() {
        return mAId;
    }

    @Override
    public boolean onLongClick(View v) {
        WebView.HitTestResult result = ((WebView) v).getHitTestResult();
        if (null == result)
            return false;
        int type = result.getType();
        if (type == WebView.HitTestResult.UNKNOWN_TYPE)
            return false;
        if (type == WebView.HitTestResult.EDIT_TEXT_TYPE)
            return true;
        ItemLongClickListener itemListener;
        switch (type) {
            case WebView.HitTestResult.EMAIL_TYPE:
                Toast.makeText(getActivity(), "email", Toast.LENGTH_SHORT).show();
                break;
            case WebView.HitTestResult.GEO_TYPE:
                Toast.makeText(getActivity(), "map", Toast.LENGTH_SHORT).show();
                break;
            case WebView.HitTestResult.IMAGE_TYPE:
                Toast.makeText(getActivity(), "image", Toast.LENGTH_SHORT).show();
                break;
            case WebView.HitTestResult.PHONE_TYPE:
                Toast.makeText(getActivity(), "phone", Toast.LENGTH_SHORT).show();
                break;
            case WebView.HitTestResult.SRC_ANCHOR_TYPE:
                mPopWindow=new ItemLongClickedPopWindow(getActivity(),ItemLongClickedPopWindow.SRC_ANCHOR_TYPE_POPUPWINDOW, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mPopWindow.showAtLocation(v, Gravity.TOP | Gravity.LEFT, (int)point_x, (int) point_y);
                itemListener=new ItemLongClickListener(result.getExtra());
                mPopWindow.getView(R.id.item_long_click_open).setOnClickListener(itemListener);
                mPopWindow.getView(R.id.item_long_click_new_window).setOnClickListener(itemListener);
                mPopWindow.getView(R.id.item_long_click_background_window).setOnClickListener(itemListener);
                mPopWindow.getView(R.id.item_long_click_copy).setOnClickListener(itemListener);
                mPopWindow.getView(R.id.item_long_click_download).setOnClickListener(itemListener);
                break;
            case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                mPopWindow = new ItemLongClickedPopWindow(getActivity(), ItemLongClickedPopWindow.SRC_IMAGE_ANCHOR_TYPE_POPUPWINDOW,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mPopWindow.showAtLocation(v, Gravity.TOP | Gravity.LEFT, (int)point_x, (int) point_y);
                itemListener=new ItemLongClickListener(result.getExtra());
                mPopWindow.getView(R.id.item_long_click_open).setOnClickListener(itemListener);
                mPopWindow.getView(R.id.item_long_click_new_window).setOnClickListener(itemListener);
                mPopWindow.getView(R.id.item_long_click_background_window).setOnClickListener(itemListener);
                mPopWindow.getView(R.id.item_long_click_copy).setOnClickListener(itemListener);
                mPopWindow.getView(R.id.item_long_click_download).setOnClickListener(itemListener);
                break;
            default:
                break;
        }
        return true;
    }

    private class ItemLongClickListener implements View.OnClickListener {
        String extra;

        public ItemLongClickListener(String extra) {
            this.extra = extra;
        }

        @Override
        public void onClick(View v) {
            mPopWindow.dismiss();
            switch (v.getId()){
                case R.id.item_long_click_open:
                    mWebView.loadUrl(extra);
                    break;
                case R.id.item_long_click_new_window:
                    ((WebActivity)getActivity()).getNewWebFragment(extra);
                    break;
                case R.id.item_long_click_background_window:
                    ((WebActivity)getActivity()).getBackWebFragment(extra);
                    break;
                case R.id.item_long_click_copy:
                    ClipboardManager cbm = (ClipboardManager)getActivity().getSystemService(CLIPBOARD_SERVICE);
                    cbm.setPrimaryClip(ClipData.newPlainText("链接",extra));
                    break;
                case R.id.item_long_click_download:
                    mPresenter.addDownload(new Download(extra));
                    break;
            }
        }
    }
}


