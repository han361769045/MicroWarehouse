package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.CartRecommendGoodsItemView_;

import com.zczczy.leo.microwarehouse.model.GoodsModel;

import org.androidannotations.annotations.EBean;

/**
 * Created by leo on 2016/7/22.
 */
@EBean
public class CartRecommendGoodsAdapter extends BaseRecyclerViewAdapter<GoodsModel> {

    @Override
    public void getMoreData(Object... objects) {
        afterGetMoreData(myRestClient.getCartRecommendedGoodsList(20));
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return CartRecommendGoodsItemView_.build(parent.getContext());
    }
}
