package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.TaskOrderAdapter;
import com.zczczy.leo.microwarehouse.model.TaskOrderModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.res.StringRes;

/**
 * @author Created by LuLeo on 2016/6/17.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/17.
 */
@EActivity(R.layout.activity_ultimate_recycler_view)
public class TaskOrderActivity extends BaseUltimateRecyclerViewActivity<TaskOrderModel> {


    @StringRes
    String text_one, text_publish_task;

    @Bean
    void setMyAdapter(TaskOrderAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @AfterViews
    void afterView() {
        myTitleBar.setTitle(text_one);
        myTitleBar.setRightText(text_publish_task);
        myTitleBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishTaskOrderActivity_.intent(TaskOrderActivity.this).startForResult(1000);
            }
        });

        myAdapter.setOnItemClickListener(new BaseUltimateRecyclerViewAdapter.OnItemClickListener<TaskOrderModel>() {

            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, TaskOrderModel obj, int position) {

            }

            @Override
            public void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });
    }

    @OnActivityResult(1000)
    void onPublished(int resultCode) {
        if (resultCode == RESULT_OK) {
            isRefresh = true;
            pageIndex = 1;
            afterLoadMore();
        }
    }

    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh, "0");
    }
}
