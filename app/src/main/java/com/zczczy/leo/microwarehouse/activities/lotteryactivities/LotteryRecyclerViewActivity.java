package com.zczczy.leo.microwarehouse.activities.lotteryactivities;

import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.zczczy.leo.microwarehouse.adapters.lotteryadapters.LotteryRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by zczczy on 2016/11/10.
 */
@EActivity
public  abstract  class LotteryRecyclerViewActivity<T> extends  BaseLotteryActivity {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    RecyclerView recycler_view;

    LinearLayoutManager linearLayoutManager;

    LotteryRecyclerViewAdapter<T> myAdapter;

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
        recycler_view.setHasFixedSize(false);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setAdapter(myAdapter);
        paint.setStrokeWidth(1);
        paint.setColor(line_color);
        itemDecoration = new HorizontalDividerItemDecoration.Builder(this).margin(0).paint(paint).build();
        recycler_view.addItemDecoration(itemDecoration);
    }

}
