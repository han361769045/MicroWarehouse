package com.zczczy.leo.microwarehouse.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.BaseUltimateRecyclerViewActivity;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;
import com.zczczy.leo.microwarehouse.activities.SearchActivity_;
import com.zczczy.leo.microwarehouse.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.GoodsAdapter;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

/**
 * Created by Leo on 2016/5/20.
 */
@EFragment(R.layout.fragment_find)
public class FindFragment extends BaseUltimateRecyclerViewFragment<GoodsModel> {


    @Click
    void text_search() {
        SearchActivity_.intent(this).start();
    }


    @Bean
    void myAdapter(GoodsAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }


    @AfterViews
    void afterView() {
        myAdapter.setOnItemClickListener(new BaseUltimateRecyclerViewAdapter.OnItemClickListener<GoodsModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, GoodsModel obj, int position) {
                GoodsDetailActivity_.intent(FindFragment.this).goodsId(obj.GoodsInfoId).start();
            }

            @Override
            public void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });

//        myTitleBar.setRightButtonOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (myTitleBar.getmRightButtonView().isSelected()) {
//                    myTitleBar.getmRightButtonView().setSelected(false);
//                    verticalItem();
//                } else {
//                    myTitleBar.getmRightButtonView().setSelected(true);
//                    horizontalItem();
//                }
//            }
//        });

        myTitleBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity_.intent(FindFragment.this).start();
            }
        });
    }

    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh, 1);
    }
}
