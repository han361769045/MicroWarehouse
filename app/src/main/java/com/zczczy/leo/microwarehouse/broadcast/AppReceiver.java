package com.zczczy.leo.microwarehouse.broadcast;

import android.content.Intent;
import android.util.Log;

import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.ReceiverAction;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.api.support.content.AbstractBroadcastReceiver;

/**
 * @author Created by LuLeo on 2016/6/16.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/16.
 */
@EReceiver
public class AppReceiver extends AbstractBroadcastReceiver {

    @Pref
    MyPrefs_ pre;

    private final String TAG = this.getClass().getSimpleName();

    @ReceiverAction(actions = Intent.ACTION_PACKAGE_ADDED)
    void appInstall(Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        Log.e(TAG, "--------安装成功" + packageName);
    }

    @ReceiverAction(actions = Intent.ACTION_PACKAGE_REPLACED)
    void appUpdate(Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        Log.e(TAG, "--------替换成功" + packageName);
    }

    @ReceiverAction(actions = Intent.ACTION_PACKAGE_REMOVED)
    void appUnInstall(Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        Log.e(TAG, "--------卸载成功" + packageName);
        Log.e(TAG, "--------卸载成功" + pre.token().get());
    }


}
