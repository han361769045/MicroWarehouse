package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.activities.ReviewActivity;
import com.zczczy.leo.microwarehouse.items.ReviewItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by leo on 2016/5/8.
 */
@EBean
public class ReviewAdapter extends BaseRecyclerViewAdapter<OrderDetailModel> {


    @RootContext
    ReviewActivity reviewActivity;

    @Override
    public void getMoreData(Object... objects) {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterGetData(myRestClient.getBeevaluatedOrder());
    }

    @UiThread
    void afterGetData(BaseModelJson<List<OrderDetailModel>> bmj) {
        if (bmj == null) {
            AndroidTool.showToast(context, no_net);
            reviewActivity.notifyUI();
        } else if (bmj.Successful) {
            clear();
            if (bmj.Data.size() > 0) {
                insertAll(bmj.Data, getItems().size());
            } else {
                reviewActivity.notifyUI();
            }
        } else {
            AndroidTool.showToast(context, bmj.Error);
            reviewActivity.notifyUI();
        }
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return ReviewItemView_.build(parent.getContext());
    }


}
