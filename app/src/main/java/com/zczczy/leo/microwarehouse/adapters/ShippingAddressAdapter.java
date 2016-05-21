package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.ShippingAddressItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.ShippingAddressModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by Leo on 2016/5/4.
 */
@EBean
public class ShippingAddressAdapter extends BaseRecyclerViewAdapter<ShippingAddressModel> {


    @Override
    public void getMoreData(Object... objects) {
//        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
//        afterGetData(myRestClient.getMReceiptAddressListByUserInfoId());
    }

    @UiThread
    void afterGetData(BaseModelJson<List<ShippingAddressModel>> bmj) {
        if (bmj == null) {
            bmj = new BaseModelJson<>();
//            AndroidTool.showToast(context, no_net);
        } else if (bmj.Successful) {
            clear();
            if (bmj.Data.size() > 0) {
                insertAll(bmj.Data, getItemCount());
            }
        }
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return ShippingAddressItemView_.build(parent.getContext());
    }
}
