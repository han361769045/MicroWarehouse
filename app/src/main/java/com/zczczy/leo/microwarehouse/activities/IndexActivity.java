package com.zczczy.leo.microwarehouse.activities;

import android.content.pm.PackageManager;
import android.support.v4.app.FragmentTransaction;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.fragments.CustomPresentationPagerFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Leo on 2016/5/20.
 */
@EActivity(R.layout.activity_index)
public class IndexActivity extends BaseActivity {


    int versionCode = 0;

    @AfterViews
    void afterView() {

        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (versionCode > pre.verCode().get()) {
            showIndex();
        } else {
            WelcomeActivity_.intent(this).start();
            finish();
        }
    }

    void showIndex() {
        int[] resIds = {R.drawable.index_one, R.drawable.index_two, R.drawable.index_three, R.drawable.index_four, R.drawable.index_five};
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, CustomPresentationPagerFragment_.builder().build());
        fragmentTransaction.commit();
    }

}
