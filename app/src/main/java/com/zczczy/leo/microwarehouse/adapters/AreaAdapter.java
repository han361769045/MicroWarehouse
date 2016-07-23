package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.AreaItemView_;
import com.zczczy.leo.microwarehouse.model.AreaModel;

import org.androidannotations.annotations.EBean;

/**
 * Created by Leo on 2016/5/4.
 */
@EBean
public class AreaAdapter extends BaseRecyclerViewAdapter<AreaModel> {


    @Override
    public void getMoreData(Object... objects) {
        afterGetMoreData(myRestClient.getAreaListByCityId(objects[0].toString()));
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return AreaItemView_.build(parent.getContext());
    }
}
