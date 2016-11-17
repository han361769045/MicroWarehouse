package com.zczczy.leo.microwarehouse.activities;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.AreaAdapter;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.model.AreaModel;
import com.zczczy.leo.microwarehouse.model.CityModel;
import com.zczczy.leo.microwarehouse.model.ProvinceModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by Leo on 2016/5/4.
 * 区域选择
 */
@EActivity(R.layout.activity_province)
public class AreaActivity extends BaseRecyclerViewActivity<AreaModel> {


    @Extra
    ProvinceModel province;

    @Extra
    CityModel city;

    @Bean
    void setAdapter(AreaAdapter myAdapter) {
        this.myAdapter = myAdapter;

    }

    @AfterViews
    void afterView() {
        myTitleBar.setTitle("区域");
        myAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<AreaModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, AreaModel obj, int position) {
                Intent intent = new Intent();
                intent.putExtra("areaId", Integer.valueOf(obj.AreaId));
                intent.putExtra("pca", province.ProvinceName + city.CityName + obj.AreaName);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        myAdapter.getMoreData(city.CityId);
    }
}
