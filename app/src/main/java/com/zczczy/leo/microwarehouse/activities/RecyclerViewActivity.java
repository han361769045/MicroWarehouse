package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/21.
 */
@EActivity
public abstract class RecyclerViewActivity extends BaseActivity {

    @ViewById
    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;

    @AfterViews
    void afterRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


}
