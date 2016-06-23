package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.ProvinceItemView_;
import com.zczczy.leo.microwarehouse.model.ProvinceModel;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

/**
 * Created by Leo on 2016/5/4.
 */
@EBean
public class ProvinceAdapter extends BaseRecyclerViewAdapter<ProvinceModel> {

    @StringRes
    String no_net;

    @Override
    public void getMoreData(Object... objects) {
        afterGetMoreData(myRestClient.getAllProvinceList());
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return ProvinceItemView_.build(parent.getContext());
    }
}
