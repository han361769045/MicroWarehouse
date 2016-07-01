package com.zczczy.leo.microwarehouse;

import android.app.Application;
import android.os.Vibrator;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.model.BannerModel;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;
import com.zczczy.leo.microwarehouse.model.NoticeInfoModel;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;
import com.zczczy.leo.microwarehouse.service.LocationService;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Leo on 2016/4/27.
 */
@EApplication
public class MyApplication extends Application {


    @Pref
    MyPrefs_ pre;

    @Bean
    MyBackgroundTask myBackgroundTask;

    //首页banner
    private List<BannerModel> newBannerList;
    //首页广告
    private List<AdvertModel> advertModelList;

    private List<GoodsTypeModel> goodsTypeModelList;

    private List<NoticeInfoModel> noticeInfoModelList;

    public IWXAPI iWXApi;

    //百度地图
    @Bean
    public LocationService locationService;

    @SystemService
    public Vibrator mVibrator;

    @AfterInject
    void afterInject() {
        advertModelList = new ArrayList<>(14);
        newBannerList = new ArrayList<>();
        goodsTypeModelList = new ArrayList<>();
        noticeInfoModelList = new ArrayList<>();

        iWXApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        iWXApi.unregisterApp();
        iWXApi.registerApp(Constants.APP_ID);
        if (pre.isFirst().get()) {
            JPushInterface.setAliasAndTags(this, "", null, myBackgroundTask);
        }
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        CrashReport.initCrashReport(getApplicationContext(), Constants.BUGLY_APP_ID, false);
    }


    public List<AdvertModel> getAdvertModelList() {
        return advertModelList;
    }

    public void setAdvertModelList(List<AdvertModel> advertModelList) {
        this.advertModelList = advertModelList;
    }


    public List<BannerModel> getNewBannerList() {
        return newBannerList;
    }

    public void setNewBannerList(List<BannerModel> newBannerList) {
        this.newBannerList = newBannerList;
    }

    public List<GoodsTypeModel> getGoodsTypeModelList() {
        return goodsTypeModelList;
    }

    public void setGoodsTypeModelList(List<GoodsTypeModel> goodsTypeModelList) {
        this.goodsTypeModelList = goodsTypeModelList;
    }

    public List<NoticeInfoModel> getNoticeInfoModelList() {
        return noticeInfoModelList;
    }

    public void setNoticeInfoModelList(List<NoticeInfoModel> noticeInfoModelList) {
        this.noticeInfoModelList = noticeInfoModelList;
    }
}
