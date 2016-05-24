package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.RecyclerView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.MemberOrderAdapter;
import com.zczczy.leo.microwarehouse.model.OrderModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;

/**
 * Created by leo on 2016/5/7.
 */
@EActivity(R.layout.activity_member_order)
public class MemberOrderActivity extends BaseUltimateRecyclerViewActivity<OrderModel> {

    @Extra
    String title;

    @Extra
    int orderState;


    @Bean
    void setMyAdapter(MemberOrderAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }


    @AfterViews
    void afterView() {
        myTitleBar.setTitle(title);
        empty_view.setText(String.format(empty_order, title));
        myAdapter.setOnItemClickListener(new BaseUltimateRecyclerViewAdapter.OnItemClickListener<OrderModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, OrderModel obj, int position) {
                OrderDetailActivity_.intent(MemberOrderActivity.this).orderId(obj.MOrderId).startForResult(1000);
            }

            @Override
            public void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });
    }

    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh, orderState);
    }

    @OnActivityResult(1000)
    void onResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            isRefresh = true;
            afterLoadMore();
        }
    }


}
