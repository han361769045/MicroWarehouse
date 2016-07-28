package com.zczczy.leo.microwarehouse.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.fragments.CartFragment_;
import com.zczczy.leo.microwarehouse.fragments.FindFragment_;
import com.zczczy.leo.microwarehouse.fragments.HomeFragment_;
import com.zczczy.leo.microwarehouse.fragments.MineFragment_;
import com.zczczy.leo.microwarehouse.listener.ReadSmsContent;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.UpdateAppModel;
import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.service.LocationService;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.viewgroup.FragmentTabHost;
import com.zczczy.leo.microwarehouse.views.AnimTextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Leo on 2016/5/20.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {


    @ViewById
    public FragmentTabHost tabHost;

    @StringArrayRes
    String[] tabTag, tabTitle;

    @ViewById(android.R.id.tabs)
    TabWidget tabWidget;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @DrawableRes
    Drawable home_selector, find_selector, cart_selector, mine_selector;

    @StringRes
    String progress_de;

    @Bean
    MyBackgroundTask myBackgroundTask;

    long firstTime = 0;

    //导航
    Class[] classTab = {HomeFragment_.class, FindFragment_.class, CartFragment_.class, MineFragment_.class};

    Drawable[] drawables = new Drawable[4];

    BaseModelJson<UpdateAppModel> appInfo;

    View view;

    AlertDialog.Builder adb;

    AlertDialog ad;

    String downloadName;

    MagicProgressCircle p_downloadApk;

    AnimTextView p_text;

    /* 下载保存路径 */
    String mSavePath;

    File apkFile;

    @AfterInject
    void afterInject() {
        drawables[0] = home_selector;
        drawables[1] = find_selector;
        drawables[2] = cart_selector;
        drawables[3] = mine_selector;
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        initTab();
        getUpdateApp();
        myBackgroundTask.setAlias();



    }


    @Background
    void getUpdateApp() {
        BaseModelJson<UpdateAppModel> bmj = myRestClient.appUpdCheck(Constants.ANDROID);
        GetUpdate(bmj);
    }

    @UiThread
    void GetUpdate(BaseModelJson<UpdateAppModel> bmj) {
        if (bmj == null) {
//            AndroidTool.showToast(this, no_net);
        } else if (bmj.Successful) {
            appInfo = bmj;
            int versionCode = 0;
            try {
                // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
                versionCode = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (bmj.Data.VersionCode > versionCode) {
                //升级
                updateNotice();
            }
        } else {
            AndroidTool.showToast(this, bmj.Error);
        }
    }

    void updateNotice() {
        AlertDialog.Builder adbr = new AlertDialog.Builder(this);
        adbr.setTitle("提示").setMessage("有新版本，请更新").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                view = layoutInflater.inflate(R.layout.new_update_dialog, null);
                p_downloadApk = (MagicProgressCircle) view.findViewById(R.id.p_downloadApk);
                p_text = (AnimTextView) view.findViewById(R.id.p_text);
                adb = new AlertDialog.Builder(MainActivity.this);
                ad = adb.setView(view).create();
                ad.setCancelable(false);
                getPermissions();
                ad.show();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setCancelable(false).create().show();
    }

    @Background
    void downloadApk() {
        try {
            // 判断SD卡是否存在，并且是否具有读写权限
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获得存储卡的路径
                String sdpath = Environment.getExternalStorageDirectory() + "/";
                mSavePath = sdpath + "download_cache";
                URL url = new URL(appInfo.Data.VersionUrl);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                // 获取文件大小
                int length = conn.getContentLength();
                // 创建输入流
                InputStream is = conn.getInputStream();
                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                downloadName = AndroidTool.getYYYYMMDDHHMMSS(new Date()) + "microWarehouse.apk";
                apkFile = new File(mSavePath, downloadName);
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                // 缓存
                byte buf[] = new byte[1024];
                // 写入到文件中
                do {
                    int numread = is.read(buf);
                    count += numread;
                    // 计算进度条位置
                    updateDownloadProgress(((float) count) / length);
                    if (numread <= 0) {
                        // 下载完成
                        pre.clear();
                        installApk();
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numread);
                } while (true);// 点击取消就停止下载.
                fos.close();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void updateDownloadProgress(float progress) {
        p_downloadApk.setPercent(progress);
        p_text.setText(String.format(progress_de, (int) (progress * 100)));
    }

    //安装apk文件
    private void installApk() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);     //浏览网页的Action(动作)
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(apkFile), type);  //设置数据类型
        startActivity(intent);
    }

    protected void initTab() {
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        for (int i = 0; i < tabTag.length; i++) {
            Bundle bundle = new Bundle();
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabTag[i]);
            tabSpec.setIndicator(buildIndicator(i));
            tabHost.addTab(tabSpec, classTab[i], bundle);
        }
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
            }
        });

    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 127);
            } else {
                downloadApk();
            }
        } else {
            downloadApk();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadApk();
        } else {
            p_downloadApk.setPercent(0);
            p_text.setText("您拒绝授权！导致应用无法更新");
        }
    }


    protected View buildIndicator(int position) {
        View view = layoutInflater.inflate(R.layout.tab_indicator, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon_tab);
        TextView textView = (TextView) view.findViewById(R.id.text_indicator);
        imageView.setImageDrawable(drawables[position]);
        textView.setText(tabTitle[position]);
        return view;
    }


    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            AndroidTool.showToast(this, "再按一次退出程序");
            firstTime = secondTime;
        } else {
            finish();
            System.exit(-1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        JPushInterface.onResume(MainActivity.this);
    }

    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(MainActivity.this);
    }
}
