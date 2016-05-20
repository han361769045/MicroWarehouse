package com.zczczy.leo.microwarehouse;

import android.app.Application;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EApplication;

/**
 * Created by Leo on 2016/4/27.
 */
@EApplication
public class MyApplication extends Application {


    @AfterInject
    void afterInject() {

    }

}
