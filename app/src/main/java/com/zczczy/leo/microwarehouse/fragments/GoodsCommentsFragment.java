package com.zczczy.leo.microwarehouse.fragments;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.GoodsCommentsAdapter;
import com.zczczy.leo.microwarehouse.model.GoodsCommentsModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

/**
 * @author Created by LuLeo on 2016/6/12.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/12.
 */
@EFragment(R.layout.activity_goods_comments)
public class GoodsCommentsFragment extends BaseUltimateRecyclerViewFragment<GoodsCommentsModel> {

    @FragmentArg
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
