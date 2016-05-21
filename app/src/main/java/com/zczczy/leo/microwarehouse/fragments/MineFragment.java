package com.zczczy.leo.microwarehouse.fragments;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/20.
 */
@EFragment(R.layout.activity_goods_detail)
public class MineFragment extends BaseFragment {
    @ViewById
    WebView web_view;


    @AfterViews
    void  afterView(){
        web_view.loadUrl("http://www.zczczy.com");

    }
}
