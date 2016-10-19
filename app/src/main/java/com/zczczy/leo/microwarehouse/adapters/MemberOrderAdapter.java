package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.BaseViewHolder;
import com.zczczy.leo.microwarehouse.items.MemberOrderItemView_;
import com.zczczy.leo.microwarehouse.model.OrderModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.EBean;

/**
 * Created by leo on 2016/5/7.
 */
@EBean
public class MemberOrderAdapter extends BaseUltimateRecyclerViewAdapter<OrderModel> {


    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterGetMoreData(myRestClient.getOrderInfoListByStatus(pageIndex, pageSize, Integer.valueOf(objects[0].toString())));
    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
        return MemberOrderItemView_.build(parent.getContext());
    }


    @Override
    void onBindHeaderViewHolder(BaseViewHolder viewHolder) {

    }

}
