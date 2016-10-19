package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.BaseViewHolder;
import com.zczczy.leo.microwarehouse.items.DepotItemView_;
import com.zczczy.leo.microwarehouse.model.DepotModel;

import org.androidannotations.annotations.EBean;

/**
 * Created by leo on 2016/5/4.
 */
@EBean
public class DepotAdapter extends BaseUltimateRecyclerViewAdapter<DepotModel> {


    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        afterGetMoreData(myRestClient.getDepotList(pageIndex, pageSize));
    }

    @Override
    void onBindHeaderViewHolder(BaseViewHolder viewHolder) {

    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
        return DepotItemView_.build(parent.getContext());
    }
}
