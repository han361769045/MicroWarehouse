package com.zczczy.leo.microwarehouse.broadcast;

import android.content.Intent;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.ReceiverAction;
import org.androidannotations.api.support.content.AbstractBroadcastReceiver;

import cn.jpush.android.api.JPushInterface;

/**
 * @author Created by LuLeo on 2016/6/15.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/15.
 */
@EReceiver
public class MyJPushReceiver extends AbstractBroadcastReceiver {

    final static String ACTION_REGISTRATION_ID = JPushInterface.ACTION_REGISTRATION_ID;


    @ReceiverAction(actions = {"ACTION_REGISTRATION_ID", Intent.ACTION_VIEW})
    void onRe() {

    }

}
