package com.zczczy.leo.microwarehouse.activities.lotteryactivities;

import android.graphics.Color;
import android.support.v7.widget.OrientationHelper;

import com.marshalchen.ultimaterecyclerview.CustomUltimateRecyclerview;
import com.marshalchen.ultimaterecyclerview.uiUtils.BasicGridLayoutManager;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.lotteryadapters.AnnouncedAdapter;
import com.zczczy.leo.microwarehouse.model.GoodsModel;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zczczy on 2016/11/15.
 * 即将揭晓
 */
@EActivity(R.layout.activity_announced_layout)
public class AnnouncedActivity extends LotteryUltimateRecyclerViewActivity<GoodsModel> {
    @ViewById
    CustomUltimateRecyclerview ultimate_recycler_view;

    BasicGridLayoutManager gridLayoutManager;


    @Bean
    void myAdapter(AnnouncedAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @AfterViews
    void afterView(){
        bus.register(this);
        myTitleBar.setTitle("即将揭晓");
        ultimate_recycler_view.setHasFixedSize(true);
        gridLayoutManager = new BasicGridLayoutManager(this, 2, OrientationHelper.VERTICAL, false, myAdapter);
        ultimate_recycler_view.setLayoutManager(gridLayoutManager);
        ultimate_recycler_view.setAdapter(myAdapter);
        paint.setStrokeWidth(0);
        paint.setColor(Color.WHITE);
    }


    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, 10, isRefresh, 1);
    }
}
