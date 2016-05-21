package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.items.ShippingAddressItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.ShippingAddressModel;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

/**
 * Created by Leo on 2016/5/4.
 */
@EBean
public class ShippingAddressAdapter extends BaseRecyclerViewAdapter<ShippingAddressModel> {

    @RestService
    MyRestClient myRestClient;

    @Pref
    MyPrefs_ pre;

    @StringRes
    String no_net;

    @Bean
    MyErrorHandler myErrorHandler;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }


    @Override
    @Background
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
