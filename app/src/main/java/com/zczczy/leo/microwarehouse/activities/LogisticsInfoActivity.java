package com.zczczy.leo.microwarehouse.activities;

import android.view.View;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.LogisticsInfoAdapter;
import com.zczczy.leo.microwarehouse.model.LogisticsInfoModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zczczy on 2016/5/5.
 * 物流信息
 */
@EActivity(R.layout.activity_logistics_info)
public class LogisticsInfoActivity extends BaseRecyclerViewActivity<LogisticsInfoModel> {

    @ViewById
    TextView empty_view;

    @Extra
    String MOrderId;

    @Bean
    void setMyAdapter(LogisticsInfoAdapter myAdapter) {
        this.myAdapter = myAdapter;

    }

    @AfterViews
    void afterView() {
        myAdapter.getMoreData(MOrderId);
    }

    public void notifyUI() {
        empty_view.setVisibility(View.VISIBLE);
        empty_view.setText(empty_logistics);
    }
}
