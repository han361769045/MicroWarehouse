package com.zczczy.leo.microwarehouse.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.fragments.CartFragment_;
import com.zczczy.leo.microwarehouse.fragments.FindFragment_;
import com.zczczy.leo.microwarehouse.fragments.HomeFragment_;
import com.zczczy.leo.microwarehouse.fragments.MineFragment_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.service.LocationService;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.viewgroup.FragmentTabHost;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by Leo on 2016/5/20.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements BDLocationListener {


    @ViewById
    FragmentTabHost tabHost;

    @StringArrayRes
    String[] tabTag, tabTitle;

    @ViewById(android.R.id.tabs)
    TabWidget tabWidget;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @DrawableRes
    Drawable home_selector, find_selector, cart_selector, mine_selector;

    long firstTime = 0;

    //导航
    Class[] classTab = {HomeFragment_.class, FindFragment_.class, CartFragment_.class, MineFragment_.class};

    Drawable[] drawables = new Drawable[4];

    LocationService locationService;

    @AfterInject
    void afterInject() {
        drawables[0] = home_selector;
        drawables[1] = find_selector;
        drawables[2] = cart_selector;
        drawables[3] = mine_selector;
        myRestClient.setRestErrorHandler(myErrorHandler);
        locationService = app.locationService;
    }

    @AfterViews
    void afterView() {
        initTab();
        locationService.registerListener(this);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
    }


    protected void initTab() {
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        for (int i = 0; i < tabTag.length; i++) {
            Bundle bundle = new Bundle();
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabTag[i]);
            tabSpec.setIndicator(buildIndicator(i));
            tabHost.addTab(tabSpec, classTab[i], bundle);
        }
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

            }
        });

    }

    protected View buildIndicator(int position) {
        View view = layoutInflater.inflate(R.layout.tab_indicator, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon_tab);
        TextView textView = (TextView) view.findViewById(R.id.text_indicator);
        imageView.setImageDrawable(drawables[position]);
        textView.setText(tabTitle[position]);
        return view;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        Log.e("11111111111111", bdLocation.getProvince() + bdLocation.getCity() + bdLocation.getAddrStr() + bdLocation.getStreet() + bdLocation.getBuildingName() + bdLocation.getLocationDescribe());
        Log.e("11111111111111", bdLocation.getAddrStr());
        Log.e("11111111111111", bdLocation.getStreet() + "");
        Log.e("11111111111111", bdLocation.getBuildingName() + "");
        Log.e("11111111111111", bdLocation.getLocationDescribe() + "");
        Log.e("11111111111111", bdLocation.getAddress().province + bdLocation.getAddress().city + bdLocation.getAddress().address + bdLocation.getAddress().street + "");

    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            AndroidTool.showToast(this, "再按一次退出程序");
            firstTime = secondTime;
        } else {
            finish();
            System.exit(-1);
        }
    }
}
