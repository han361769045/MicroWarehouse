package com.zczczy.leo.microwarehouse.activities;

import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.leo.lu.llrecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.leo.lu.mytitlebar.MyTitleBar;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by Leo on 2016/5/21.
 */
@EActivity
public abstract class BaseRecyclerViewActivity<T> extends BaseActivity {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;

    BaseRecyclerViewAdapter<T> myAdapter;

    Paint paint = new Paint();

    RecyclerView.ItemDecoration itemDecoration;

    @Bean
    MyErrorHandler myErrorHandler;

    @RestService
    MyRestClient myRestClient;


    @AfterInject
    void afterRecyclerInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterRecyclerView() {
        AndroidTool.showLoadDialog(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
        paint.setStrokeWidth(1);
        paint.setColor(line_color);
        itemDecoration = new HorizontalDividerItemDecoration.Builder(this).margin(0).paint(paint).build();
        recyclerView.addItemDecoration(itemDecoration);
    }


}
