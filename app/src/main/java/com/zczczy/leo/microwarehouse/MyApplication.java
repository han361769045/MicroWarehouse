package com.zczczy.leo.microwarehouse;

import android.app.Application;
import android.os.Vibrator;

import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.model.BannerModel;
import com.zczczy.leo.microwarehouse.service.LocationService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EApplication;

import java.util.List;

/**
 * Created by Leo on 2016/4/27.
 */
@EApplication
public class MyApplication extends Application {

    //首页banner
    private List<BannerModel> newBannerList;
    //首页广告
    private List<AdvertModel> advertModelList;

    //百度地图
    public LocationService locationService;
    public Vibrator mVibrator;

    @AfterInject
    void afterInject() {

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
}
