package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.CityItemView_;
import com.zczczy.leo.microwarehouse.model.CityModel;

import org.androidannotations.annotations.EBean;

/**
 * Created by Leo on 2016/5/4.
 */
@EBean
public class CityAdapter extends BaseRecyclerViewAdapter<CityModel> {


    @Override
    public void getMoreData(Object... objects) {
        afterGetMoreData(myRestClient.getCityListByProvinceId(objects[0].toString()));
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return CityItemView_.build(parent.getContext());
    }
}
