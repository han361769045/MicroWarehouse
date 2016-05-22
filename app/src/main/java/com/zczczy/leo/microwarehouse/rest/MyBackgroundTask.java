package com.zczczy.leo.microwarehouse.rest;

import android.content.Context;
import android.net.ConnectivityManager;

import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.model.BannerModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
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

import java.util.List;

/**
 * Created by Leo on 2016/4/29.
 */
@EBean
public class MyBackgroundTask {

    @RootContext
    Context context;

    @StringRes
    String no_net;

    @SystemService
    ConnectivityManager connManager;

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

}
