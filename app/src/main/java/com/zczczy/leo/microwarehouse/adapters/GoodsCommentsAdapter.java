package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.BaseViewHolder;
import com.zczczy.leo.microwarehouse.items.GoodsCommentsItemView_;
import com.zczczy.leo.microwarehouse.model.GoodsCommentsModel;

import org.androidannotations.annotations.EBean;

/**
 * Created by Leo on 2016/5/1.
 */
@EBean
public class GoodsCommentsAdapter extends BaseUltimateRecyclerViewAdapter<GoodsCommentsModel> {


    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        afterGetMoreData(myRestClient.getGoodsCommentsByGoodsInfoId(pageIndex, pageSize, (objects[0]).toString()));
    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
        return GoodsCommentsItemView_.build(parent.getContext());
    }

    @Override
    void onBindHeaderViewHolder(BaseViewHolder viewHolder) {

    }

}
