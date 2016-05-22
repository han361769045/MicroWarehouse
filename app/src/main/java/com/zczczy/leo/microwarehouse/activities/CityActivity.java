package com.zczczy.leo.microwarehouse.activities;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.CityAdapter;
import com.zczczy.leo.microwarehouse.model.CityModel;
import com.zczczy.leo.microwarehouse.model.ProvinceModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;

/**
 * Created by Leo on 2016/5/4.
 */
@EActivity(R.layout.activity_province)
public class CityActivity extends BaseRecyclerViewActivity<CityModel> {


    @Extra
    ProvinceModel province;


    @Bean
    void setMyAdapter(CityAdapter myAdapter) {
        this.myAdapter = myAdapter;

    }

    @AfterViews
    void afterView() {
        myTitleBar.setTitle("城市");
        myAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<CityModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, CityModel obj, int position) {
                AreaActivity_.intent(CityActivity.this).province(province).city(obj).startForResult(1000);
            }
        });
        myAdapter.getMoreData(province.ProvinceId);
    }

    @OnActivityResult(1000)
    void onSelectPCA(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
