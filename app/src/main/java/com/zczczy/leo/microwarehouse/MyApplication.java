package com.zczczy.leo.microwarehouse;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.model.BannerModel;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;
import com.zczczy.leo.microwarehouse.service.LocationService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.SystemService;

import java.util.ArrayList;
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

    private List<GoodsTypeModel> goodsTypeModelList;


    //百度地图
    @Bean
    public LocationService locationService;

    @SystemService
    public Vibrator mVibrator;

    @AfterInject
    void afterInject() {
        advertModelList = new ArrayList<>(9);
        newBannerList = new ArrayList<>();
        goodsTypeModelList = new ArrayList<>();
        //百度地图
//        locationService = new LocationService(this);
//        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
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
}
