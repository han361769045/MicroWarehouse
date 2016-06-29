package com.zczczy.leo.microwarehouse.activities;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SeekBar;


import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.OrderModel;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/6.
 */
@EActivity(R.layout.activity_umspay)
public class UmspayActivity extends BaseActivity {

    @ViewById
    WebView wv_web;

    @ViewById
    SeekBar sb_process;

    @Extra
    OrderModel order;

    @AfterViews
    void afterView() {
        WebSettings settings = wv_web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);//设定支持缩放
        String postData = "merSign=" + order.merSign +
                "&chrCode=" + order.chrCode +
                "&tranId=" + order.transId +
                "&mchantUserCode=" + pre.token().get() +
                "&url=http://www.zczczy.com" +
                "&bankName=&cardType=";
        wv_web.postUrl(Constants.PAY_URL, postData.getBytes());
        wv_web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("onPageFinished", url);
                AndroidTool.dismissLoadDialog();
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("onPageStarted", url);
                if (url.contains("http://www.zczczy.com")) {
//                    OrderDetailActivity_.intent(UmspayActivity.this).orderId(order.MOrderId).start();
                    Log.e("onPageStarted", url);
                    finish();
                } else {
                    AndroidTool.showLoadDialog(UmspayActivity.this);
                }
            }
        });
        wv_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    sb_process.setVisibility(View.GONE);
                } else {
                    sb_process.setProgress(newProgress);
                }
            }
        });
    }

    //改写物理按键——返回的逻辑
    //如果希望浏览的网页后退而不是退出浏览器，需要WebView覆盖URL加载，让它自动生成历史访问记录，那样就可以通过前进或后退访问已访问过的站点。
    @Override
    public void onBackPressed() {
        super.finish();
        finish();
    }
}
