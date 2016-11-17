package com.zczczy.leo.microwarehouse.rest;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.activities.BaseActivity;
import com.zczczy.leo.microwarehouse.activities.LoginActivity;
import com.zczczy.leo.microwarehouse.activities.OrderDetailActivity_;
import com.zczczy.leo.microwarehouse.activities.TakeOrderActivity;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.model.BannerModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.NoticeInfoModel;
import com.zczczy.leo.microwarehouse.model.PayResult;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Leo on 2016/4/29.
 */
@EBean
public class MyBackgroundTask implements TagAliasCallback {

    private static final String TAG = "JPush";

    @RootContext
    Context context;

    @StringRes
    String no_net;

    @SystemService
    ConnectivityManager connectivityManager;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @App
    MyApplication app;

    @Bean
    OttoBus bus;

    @Pref
    MyPrefs_ pre;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }





    /**
     * 获取首页banner
     */
    @Background
    public void getHomeBanner() {
        afterGetHomeBanner(myRestClient.getHomeBanner());
    }

    @UiThread
    void afterGetHomeBanner(BaseModelJson<List<BannerModel>> bmj) {
        if (bmj != null && bmj.Successful) {
            app.setNewBannerList(bmj.Data);
        } else {
            bmj = new BaseModelJson<>();
        }
        bus.post(bmj);


    }



    /**
     * 获取首页广告
     */
    @Background
    public void getAdvertByKbn() {
        afterGetAdvertByKbn(myRestClient.getAdvertByKbn(Constants.HOME_AD));
    }

    @UiThread
    void afterGetAdvertByKbn(BaseModelJson<List<AdvertModel>> bmj) {
        if (bmj != null && bmj.Successful) {
            app.setAdvertModelList(bmj.Data);
        } else {
            bmj = new BaseModelJson<>();
        }
        bus.post(bmj);
    }

    /**
     * 获取首页banner
     */
    @Background
    public void getNoticeInfoList() {
        afterGetNoticeInfoList(myRestClient.getNoticeInfoList());
    }

    @UiThread
    void afterGetNoticeInfoList(BaseModelJson<List<NoticeInfoModel>> bmj) {
        if (bmj != null && bmj.Successful) {
            app.setNoticeInfoModelList(bmj.Data);
        } else {
            bmj = new BaseModelJson<>();
        }
        bus.post(bmj);
    }


    @Background
    public void aliPay(String payInfo, Activity activity, String orderId) {
        PayTask aliPay = new PayTask(activity);
        afterAliPay(aliPay.pay(payInfo, true), activity, orderId);
    }

    @UiThread
    void afterAliPay(String result, Activity activity, String orderId) {
        AndroidTool.dismissLoadDialog();
        PayResult payResult = new PayResult(result);
        /**
         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
         * docType=1) 建议商户依赖异步通知
         */
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        switch (resultStatus) {
            case "9000":
                AndroidTool.showToast(context, "支付成功");
                OrderDetailActivity_.intent(activity).orderId(orderId).start();
                activity.finish();
                break;
            case "8000":
                AndroidTool.showToast(context, "支付结果确认中");
                OrderDetailActivity_.intent(activity).orderId(orderId).start();
                activity.finish();
                break;
            case "4000":
                AndroidTool.showToast(context, "订单支付失败");
                OrderDetailActivity_.intent(activity).orderId(orderId).start();
                activity.finish();
                break;
            case "6001":
                AndroidTool.showToast(context, "用户中途取消");
                OrderDetailActivity_.intent(activity).orderId(orderId).start();
                activity.finish();
                break;
            case "6002":
                AndroidTool.showToast(context, "网络连接出错");
                OrderDetailActivity_.intent(activity).orderId(orderId).start();
                activity.finish();
                break;
            default: {
                AndroidTool.showToast(context, "网络连接出错");
                OrderDetailActivity_.intent(activity).orderId(orderId).start();
                activity.finish();
            }
        }
    }

    @Background
    public void setAlias() {
        if (!StringUtils.isEmpty(pre.jPushAlias().get()) && !pre.isSetAlias().get()) {
            JPushInterface.setAliasAndTags(context.getApplicationContext(), pre.jPushAlias().get(), null, this);
        }
    }

    @Background
    public void registerAlias() {
        if (StringUtils.isEmpty(pre.jPushAlias().get())) {
            JPushInterface.setAliasAndTags(context.getApplicationContext(), "", null, this);
        }
    }


    @Override
    @UiThread
    public void gotResult(int code, String alias, Set<String> tags) {
        String logs;
        switch (code) {
            case 0:
                if (StringUtils.isEmpty(alias)) {
                    pre.isFirst().put(false);
                } else {
                    pre.isSetAlias().put(true);
                }
                logs = "Set tag and alias success";
                Log.e(TAG, logs);
                break;
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                Log.e(TAG, logs);
                if (isNetworkAvailable()) {
                    setAlias();
                } else {
                    Log.e(TAG, "No network");
                }
                break;

            default:
                logs = "Failed with errorCode = " + code;
                Log.e(TAG, logs);
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
}