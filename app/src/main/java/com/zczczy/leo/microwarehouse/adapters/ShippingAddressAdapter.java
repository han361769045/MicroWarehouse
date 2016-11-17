package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.ShippingAddressItemView_;

import com.zczczy.leo.microwarehouse.model.ShippingAddressModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.EBean;

/**
 * Created by Leo on 2016/5/4.
 */
@EBean
public class ShippingAddressAdapter extends BaseRecyclerViewAdapter<ShippingAddressModel> {


    @Override
    public void getMoreData(Object... objects) {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterGetMoreData(myRestClient.getMReceiptAddressListByUserInfoId());
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return ShippingAddressItemView_.build(parent.getContext());
    }

    @Override
    public void itemNotify(Object... objects) {

    }
}
