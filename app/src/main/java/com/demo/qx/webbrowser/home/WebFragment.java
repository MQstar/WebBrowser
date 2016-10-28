package com.demo.qx.webbrowser.home;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.qx.webbrowser.R;
import com.demo.qx.webbrowser.custom.MyAppWebViewClient;
import com.demo.qx.webbrowser.custom.MyWebChromeClient;
import com.demo.qx.webbrowser.custom.MyWebView;
import com.demo.qx.webbrowser.data.WebPage;

import static com.demo.qx.webbrowser.R.id.action_new_web;
import static com.demo.qx.webbrowser.R.id.progressBar;

/**
 * Created by qx on 16/10/24.
 */

public class WebFragment extends Fragment implements WebContract.View, View.OnClickListener, TextView.OnEditorActionListener{
    boolean isLoading;
    boolean isTyping;
    WebContract.Presenter mPresenter;
    MyWebView mWebView;
    EditText mEditText;
    TextView mShowAddress;
    TextView mRefresh;
    TextView mMultWindow;
    ProgressBar mProgressBar;
    View root;
    android.support.v7.app.ActionBar mActionBar;
    PropertyValuesHolder pvhX;
    PropertyValuesHolder pvhY;
    ObjectAnimator scale;
    String mCurrentTitle;
    static String URL;
    private WebView.WebViewTransport mTransport;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_web, container, false);



        mActionBar=((WebActivity)getActivity()).ab;
        mRefresh=((WebActivity)getActivity()).mRefresh;
        mEditText=((WebActivity)getActivity()).mEditText;
        mShowAddress= ((WebActivity)getActivity()).mShowAddress;
        mRefresh.setOnClickListener(this);
        mShowAddress.setOnClickListener(this);
        mEditText.setOnEditorActionListener(this);
        mEditText.setSelectAllOnFocus(true);
        mProgressBar = (ProgressBar) root.findViewById(progressBar);
        mWebView = (MyWebView) root.findViewById(R.id.web_view);
        mWebView.setWebChromeClient(new MyWebChromeClient((WebActivity) getActivity(),mPresenter));
        mWebView.setWebViewClient(new MyAppWebViewClient());
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

    @Override
    public void setPresenter(@NonNull WebContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.base_toolbar_menu, menu);
    }


    //// FIXME: 16/10/25 back
   /* public void onBackPressed() {
        if (isTyping) {
            hideEdit();
        } else if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
           // super.onBackPressed();
        }
    }*/

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
                isTyping = true;
                mActionBar.hide();
                mEditText.setText(mWebView.getUrl());
                mEditText.setVisibility(View.VISIBLE);
                mEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, 0);
                break;
            default:
                break;
        }
    }

    /*@Override
    public void onPrepareOptionsMenu(Menu menu) {
        ((TextView)menu.findItem(action_new_web).getActionView()).setText(MyApp.sWebFragmentList.size()+"");
        super.onPrepareOptionsMenu(menu);
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (mWebView.canGoBack())
                mWebView.goBack();
                return true;
            /*
                pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.6f);
                pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.6f);
                scale = ObjectAnimator.ofPropertyValuesHolder(mWebView, pvhX, pvhY);
                scale.setDuration(50);
                scale.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                scale.start();
                break;*/
            case action_new_web:
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
        isTyping = false;
        mActionBar.show();
        mEditText.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
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
        }
    }

    public void setTransport(WebView.WebViewTransport transport) {
        mTransport = transport;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroyDrawingCache();
    }
}


