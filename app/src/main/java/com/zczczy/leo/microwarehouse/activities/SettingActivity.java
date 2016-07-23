package com.zczczy.leo.microwarehouse.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Switch;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author Created by LuLeo on 2016/7/23.
 *         you can contact me at :361769045@qq.com
 * @since 2016/7/23.
 */
@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @ViewById
    Switch switch_push;

    @Bean
    MyBackgroundTask myBackgroundTask;


    @CheckedChange
    void switch_push(boolean isChecked) {
        AndroidTool.showToast(this, isChecked + "");
    }

    @Click
    void btn_exit() {
        if (checkUserIsLogin()) {
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
}
