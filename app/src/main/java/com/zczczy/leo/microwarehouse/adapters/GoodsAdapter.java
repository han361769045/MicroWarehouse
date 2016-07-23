package com.zczczy.leo.microwarehouse.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.BaseUltimateViewHolder;
import com.zczczy.leo.microwarehouse.items.GoodsHorizontalItemView_;
import com.zczczy.leo.microwarehouse.items.GoodsVerticalItemView_;
import com.zczczy.leo.microwarehouse.items.ItemView;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.PagerResult;

import org.androidannotations.annotations.EBean;

/**
 * Created by Leo on 2016/5/21.
 */
@EBean
public class GoodsAdapter extends BaseUltimateRecyclerViewAdapter<GoodsModel> {


    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        BaseModelJson<PagerResult<GoodsModel>> result = null;
        switch (Integer.valueOf(objects[0].toString())) {
            case 0:
                result = myRestClient.getGoodsInfoLikeWord(pageIndex, pageSize, objects[1].toString(), objects[2].toString());
                break;
            case 1:
                result = myRestClient.getRecommendedGoods(pageIndex, pageSize);
                break;
        }
        afterGetMoreData(result);
    }

    @Override
    void onBindHeaderViewHolder(BaseUltimateViewHolder viewHolder) {

    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
        ItemView<GoodsModel> itemView = null;
        switch (verticalAndHorizontal) {
            case Horizontal:
                itemView = GoodsHorizontalItemView_.build(context);
                break;
            case Vertical:
                itemView = GoodsVerticalItemView_.build(context);
                break;
        }
        return itemView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
