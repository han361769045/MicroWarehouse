package com.zczczy.leo.microwarehouse.activities;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.ProvinceAdapter;
import com.zczczy.leo.microwarehouse.model.ProvinceModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

/**
 * Created by Leo on 2016/5/4.
 */
@EActivity(R.layout.activity_province)
public class ProvinceActivity extends BaseRecyclerViewActivity<ProvinceModel> {

    @Bean
    void setMyAdapter(ProvinceAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }


    @AfterViews
    void afterView() {
        myAdapter.getMoreData();
        myAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<ProvinceModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, ProvinceModel obj, int position) {
                CityActivity_.intent(ProvinceActivity.this).province(obj).startForResult(1000);
            }
        });
    }

    @OnActivityResult(1000)
    void onSelectPCA(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
