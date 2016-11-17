package com.zczczy.leo.microwarehouse.fragments;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.CommonCategoryAdapter;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/21.
 */
@EFragment(R.layout.fragment_common_category)
public class CommonCategoryFragment extends BaseUltimateRecyclerViewFragment<GoodsModel> {

    @FragmentArg
    String goodsId, orderBy, priceMin, priceMax;

    @Bean
    void setMyAdapter(CommonCategoryAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @AfterViews
    void afterView() {
        bus.register(this);
        myAdapter.setOnItemClickListener(new BaseUltimateRecyclerViewAdapter.OnItemClickListener<GoodsModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, GoodsModel obj, int position) {
                GoodsDetailActivity_.intent(CommonCategoryFragment.this).goodsId(obj.GoodsInfoId).start();
            }

            @Override
            public void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });
//        horizontalItem();
    }

    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh, goodsId, orderBy, priceMin, priceMax);
    }

    @Subscribe
    public void notifyUI(Integer number) {
        if (number == 1001) {
            ultimateRecyclerView.mPtrFrameLayout.refreshComplete();
        }
    }
}
