package com.demo.qx.webbrowser;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import com.demo.qx.webbrowser.custom.MyAppWebViewClient;
import com.demo.qx.webbrowser.custom.MyWebChromeClient;
import com.demo.qx.webbrowser.custom.MyWebView;

import static com.demo.qx.webbrowser.R.id.progressBar;

/**
 * Created by qx on 16/10/24.
 */

public class WebFragment extends Fragment implements MainContract.View, View.OnClickListener, TextView.OnEditorActionListener, Toolbar.OnMenuItemClickListener {
    boolean isLoading;
    boolean isTyping;
    MainContract.Presenter mPresenter;
    MyWebView mWebView;
    EditText mEditText;
    TextView mShowAddress;
    TextView mRefresh;
    ProgressBar mProgressBar;
    View root;
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
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(this);
        ((AppCompatActivity)getActivity()).getSupportActionBar();
        mRefresh = (TextView) root.findViewById(R.id.button_refresh);
        mRefresh.setOnClickListener(this);
        mShowAddress = (TextView) root.findViewById(R.id.show_address);
        mShowAddress.setOnClickListener(this);
        mEditText = (EditText) root.findViewById(R.id.address);
        mEditText.setOnEditorActionListener(this);
        mEditText.setSelectAllOnFocus(true);
        mProgressBar = (ProgressBar) root.findViewById(progressBar);
        mWebView = (MyWebView) root.findViewById(R.id.web_view);
        mWebView.setWebChromeClient(new MyWebChromeClient((MainActivity) getActivity(),mPresenter));
        mWebView.setWebViewClient(new MyAppWebViewClient());

      /*  mWebView.setOnTouchListener ( new View.OnTouchListener () {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction ()) {
                    case MotionEvent.ACTION_DOWN :
                    case MotionEvent.ACTION_UP :
                        if (!v.hasFocus ()) {
                            v.requestFocus ();
                        }
                        break ;
                }
                return false ;
            }
        });*/

        if (mTransport!=null)
        {
            mTransport.setWebView(mWebView);
            mTransport=null;
        }else
            mWebView.loadUrl(URL);
        return root;
    }

    public static WebFragment newInstance(int index,String url) {
        URL=url;
        WebFragment f = new WebFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void setPresenter(@NonNull MainContract.Presenter presenter) {
        mPresenter = presenter;
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
                mShowAddress.setVisibility(View.GONE);
                mRefresh.setVisibility(View.GONE);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_window:
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
                break;
            case R.id.action_new_web:
                break;
            case R.id.action_forward:
            if (mWebView.canGoForward())
                mWebView.goForward();
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
        mShowAddress.setVisibility(View.VISIBLE);
        mRefresh.setVisibility(View.VISIBLE);
        mEditText.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public void changeProgress(int progress) {
        if (progress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE) {
            isLoading = true;
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            mRefresh.setBackgroundResource(R.drawable.ic_clear_black_36dp);
            mProgressBar.setProgress(progress);
        }
        if (progress == 100) {
            isLoading = false;
            if (mCurrentTitle!=null)
                mShowAddress.setText(mCurrentTitle);
            mProgressBar.setVisibility(ProgressBar.GONE);
            mRefresh.setBackgroundResource(R.drawable.ic_sync_black_36dp);
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


