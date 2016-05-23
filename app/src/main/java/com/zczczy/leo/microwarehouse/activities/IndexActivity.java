package com.zczczy.leo.microwarehouse.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;

/**
 * Created by Leo on 2016/5/20.
 */
@EActivity(R.layout.activity_index)
public class IndexActivity extends BaseActivity {

    @Bean
    MyBackgroundTask myBackgroundTask;

    @Bean
    OttoBus bus;

    final int SDK_PERMISSION_REQUEST = 127;

    int i = 0;

    @AfterViews
    void afterView() {
        bus.register(this);
        myBackgroundTask.getAdvertByKbn();
        myBackgroundTask.getHomeBanner();
    }

    @Override
    public void finish() {
        bus.unregister(this);
        super.finish();
    }

    @Subscribe
    public void notifyUI(BaseModel bm) {
        i++;
        if (i == 2) {
            getPermissions();
        }
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_SMS);
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            } else {
                MainActivity_.intent(this).start();
                finish();
            }
        } else {
            MainActivity_.intent(this).start();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivity_.intent(this).start();
        finish();
    }

}
