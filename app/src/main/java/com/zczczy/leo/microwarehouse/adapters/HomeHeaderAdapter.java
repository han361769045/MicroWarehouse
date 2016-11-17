package com.zczczy.leo.microwarehouse.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.BaseUltimateViewHolder;
import com.zczczy.leo.microwarehouse.items.ItemHeaderBg_;

import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.PagerResult;

import org.androidannotations.annotations.EBean;

/**
 * Created by zczczy on 2016/9/29.
 */
@EBean
public class HomeHeaderAdapter extends BaseUltimateRecyclerViewAdapter<GoodsModel> {
    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        BaseModelJson<PagerResult<GoodsModel>> result = null;
        result = myRestClient.getRecommendedGoods(pageIndex, pageSize);
        afterGetMoreData(result);
    }

    @Override
    void onBindHeaderViewHolder(BaseUltimateViewHolder viewHolder) {

    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
        return ItemHeaderBg_.build(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
