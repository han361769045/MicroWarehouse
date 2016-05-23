package com.zczczy.leo.microwarehouse;

import android.app.Application;
import android.os.Vibrator;

import com.zczczy.leo.microwarehouse.model.Banner;
import com.zczczy.leo.microwarehouse.service.LocationService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EApplication;

import java.util.List;

/**
 * Created by Leo on 2016/4/27.
 */
@EApplication
public class MyApplication extends Application {


    //百度地图
    public LocationService locationService;
    public Vibrator mVibrator;
    public static final String ANDROID="1";
    private List<Banner> BannerList;

    public List<Banner> getBannerList() {
        return BannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        BannerList = bannerList;
    }

    @AfterInject
    void afterInject() {

    }

}
