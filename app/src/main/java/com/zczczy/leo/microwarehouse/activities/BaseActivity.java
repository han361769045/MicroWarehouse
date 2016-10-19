package com.zczczy.leo.microwarehouse.activities;

import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.util.StringUtils;

/**
 * Created by Leo on 2016/4/27.
 */
@EActivity
public abstract class BaseActivity extends AppCompatActivity {

    @SystemService
    InputMethodManager inputMethodManager;

    @SystemService
    ConnectivityManager connectivityManager;

    @SystemService
    LayoutInflater layoutInflater;

    @Pref
    MyPrefs_ pre;

    @App
    MyApplication app;

    @ColorRes
    int line_color;

    @StringRes
    String no_net;

    @StringRes
    String empty_search, empty_order, empty_review, empty_no_review, empty_logistics;

    Paint paint = new Paint();

    /**
     * 判断用户是否登录
     *
     * @return
     */
    protected boolean checkUserIsLogin() {
        return !StringUtils.isEmpty(pre.token().get());
    }

    public void finish() {
        closeInputMethod();
        super.finish();
    }


    //隐藏软键盘
    void closeInputMethod() {
        /*隐藏软键盘*/
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        }
    }

    //隐藏软键盘
    void closeInputMethod(View editText) {
        /*隐藏软键盘*/
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }
    }
}
