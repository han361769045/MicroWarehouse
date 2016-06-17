package com.zczczy.leo.microwarehouse.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.TaskOrderAdapter;
import com.zczczy.leo.microwarehouse.model.TaskOrderModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

/**
 * @author Created by LuLeo on 2016/6/17.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/17.
 */
@EFragment(R.layout.activity_ultimate_recycler_view)
public class MemberTaskOrderFragment extends BaseUltimateRecyclerViewFragment<TaskOrderModel> {

    @FragmentArg
    String type;


    @Bean
    void setMyAdapter(TaskOrderAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @AfterViews
    void afterView() {
        myTitleBar.setVisibility(View.GONE);
        myAdapter.setOnItemClickListener(new BaseUltimateRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, Object obj, int position) {

            }

            @Override
            public void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });
    }

    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh, "1", type);
    }
}
