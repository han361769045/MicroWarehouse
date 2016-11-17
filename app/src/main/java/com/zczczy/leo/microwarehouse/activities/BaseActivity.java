package com.zczczy.leo.microwarehouse.activities;


import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;



import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;

import org.androidannotations.annotations.AfterViews;
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




    @AfterViews
    void baseAfterView(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明状态栏
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }


    /**
     * 检查当前网络是否可用
     */
    public boolean isNetworkAvailable() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

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
