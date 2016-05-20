package com.zczczy.leo.microwarehouse.activities;

import com.zczczy.leo.microwarehouse.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Leo on 2016/5/20.
 */
@EActivity(R.layout.activity_index)
public class IndexActivity extends BaseActivity {


    @AfterViews
    void afterView() {
        MainActivity_.intent(this).start();
    }
}
