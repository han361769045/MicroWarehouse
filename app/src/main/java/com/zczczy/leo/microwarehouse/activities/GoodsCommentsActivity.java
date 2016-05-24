package com.zczczy.leo.microwarehouse.activities;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.GoodsCommentsAdapter;
import com.zczczy.leo.microwarehouse.model.GoodsCommentsModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by Leo on 2016/5/1.
 */
@EActivity(R.layout.activity_goods_comments)
public class GoodsCommentsActivity extends BaseUltimateRecyclerViewActivity<GoodsCommentsModel> {


    @Extra
    String goodsId;

    @Bean
    void setMyAdapter(GoodsCommentsAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }


    @AfterViews
    void afterView() {
        empty_view.setText(empty_review);
    }

    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh, goodsId);
    }

}
