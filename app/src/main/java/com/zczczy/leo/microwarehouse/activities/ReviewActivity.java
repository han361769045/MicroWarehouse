package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;

import com.zczczy.leo.microwarehouse.adapters.ReviewAdapter;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;


/**
 * Created by leo on 2016/5/8.
 */
@EActivity(R.layout.activity_review)
public class ReviewActivity extends BaseRecyclerViewActivity<OrderDetailModel> {


    @ViewById
    TextView empty_view;


    @Bean
    void setMyAdapter(ReviewAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }


    @AfterViews
    void afterView() {
        myAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<OrderDetailModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, OrderDetailModel obj, int position) {
                PublishReviewActivity_.intent(ReviewActivity.this).model(obj).startForResult(1000);
            }
        });
        myAdapter.getMoreData();

    }

    void afterLoadMore() {
        myAdapter.getMoreData();
    }

    @OnActivityResult(1000)
    void afterReview(int resultCode) {
        if (resultCode == RESULT_OK) {
            afterLoadMore();
        }
    }

    public void notifyUI() {
        empty_view.setVisibility(View.VISIBLE);
        empty_view.setText(empty_no_review);
    }
}
