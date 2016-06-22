package com.zczczy.leo.microwarehouse.broadcast;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.zczczy.leo.microwarehouse.activities.OrderDetailActivity_;
import com.zczczy.leo.microwarehouse.activities.TaskOrderDetailActivity_;
import com.zczczy.leo.microwarehouse.model.NotificationModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.ReceiverAction;
import org.androidannotations.api.support.content.AbstractBroadcastReceiver;

/**
 * @author Created by LuLeo on 2016/6/15.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/15.
 */
@EReceiver
public class MyJPushReceiver extends AbstractBroadcastReceiver {

    Gson gson = new Gson();

    /**
     * 接收到通知
     *
     * @param extra 额外参数
     */
    @ReceiverAction(actions = {"cn.jpush.android.intent.NOTIFICATION_RECEIVED"})
    void onReceivedNotification(@ReceiverAction.Extra("cn.jpush.android.EXTRA") String extra) {
        Log.e("", extra);
        gson.fromJson(extra, NotificationModel.class);
    }

    /**
     * 当用户点击通知时
     *
     * @param context context
     * @param extra   额外参数
     */
    @ReceiverAction(actions = {"cn.jpush.android.intent.NOTIFICATION_OPENED"})
    void onOpenNotification(Context context, @ReceiverAction.Extra("cn.jpush.android.EXTRA") String extra) {
        NotificationModel model = gson.fromJson(extra, NotificationModel.class);
        if (model != null) {
            if (Constants.KBN_ORDER.equals(model.kbn)) {
                if (context instanceof OrderDetailActivity_) {
                    ((OrderDetailActivity_) context).finish();
                }
                OrderDetailActivity_.intent(context).flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .orderId(model.InfoId)
                        .start();
            }
            if (Constants.KBN_SERVICE.equals(model.kbn)) {
                if (context instanceof OrderDetailActivity_) {
                    ((TaskOrderDetailActivity_) context).finish();
                }
                TaskOrderDetailActivity_.intent(context).flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .TaskOrderId(model.InfoId)
                        .start();
            }
        }
    }

    /**
     * 接收自定义消息
     *
     * @param message 自定义消息内容
     * @param extra   额外参数
     */
    @ReceiverAction(actions = {"cn.jpush.android.intent.MESSAGE_RECEIVED"})
    void onReceivedMessage(@ReceiverAction.Extra("cn.jpush.android.MESSAGE") String message, @ReceiverAction.Extra("cn.jpush.android.EXTRA") String extra) {
    }

}
