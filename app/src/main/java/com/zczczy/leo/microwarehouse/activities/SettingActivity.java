package com.zczczy.leo.microwarehouse.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.DataCleanManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import cn.jpush.android.api.JPushInterface;

/**
 * @author Created by LuLeo on 2016/7/23.
 *         you can contact me at :361769045@qq.com
 * @since 2016/7/23.
 */
@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @ViewById
    TextView txt_cache;

    @ViewById
    Switch switch_push;

    @Bean
    MyBackgroundTask myBackgroundTask;

    @AfterViews
    void afterView() {
        txt_cache.setText(DataCleanManager.getTotalCacheSize(this));
        switch_push.setChecked(pre.isStartPush().get());
    }

    @CheckedChange
    void switch_push(boolean isChecked) {
        if (isChecked && !pre.isStartPush().get()) {
            JPushInterface.resumePush(this.getApplicationContext());
        } else if (!isChecked && pre.isStartPush().get()) {
            JPushInterface.stopPush(this.getApplicationContext());
        }
        pre.isStartPush().put(isChecked);
    }

    @Click
    void ll_clear_cache() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("提示").setMessage("确定要清除本地缓存吗？").setPositiveButton("清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataCleanManager.clearAllCache(SettingActivity.this);
                txt_cache.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));
            }
        }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
    }


    @Click
    void btn_exit() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("提示").setMessage("确定要注销吗？").setPositiveButton("注销", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int verCode = pre.verCode().get();
                pre.clear();
                pre.verCode().put(verCode);
                myBackgroundTask.registerAlias();
                finish();

            }
        }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
    }
}
