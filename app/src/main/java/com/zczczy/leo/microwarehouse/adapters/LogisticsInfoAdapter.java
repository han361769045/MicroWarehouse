package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;


import com.zczczy.leo.microwarehouse.activities.LogisticsInfoActivity;
import com.zczczy.leo.microwarehouse.items.LogisticsInfoItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.LogisticsInfoModel;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.List;


/**
 * Created by zczczy on 2016/5/5.
 */
@EBean
public class LogisticsInfoAdapter extends BaseRecyclerViewAdapter<LogisticsInfoModel> {


    @RootContext
    LogisticsInfoActivity mLogisticsInfoActivity;


    @Override
    public void getMoreData(Object... objects) {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        BaseModelJson<List<LogisticsInfoModel>> bmj = myRestClient.getLogistics(objects[0].toString());
        afterGetData(bmj);
    }

    @UiThread
    void afterGetData(BaseModelJson<List<LogisticsInfoModel>> bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj != null && bmj.Successful) {
            if (bmj.Data.size() > 0) {
                bmj.Data.get(0).isLast = true;
                insertAll(bmj.Data, getItems().size());
            } else {
                mLogisticsInfoActivity.notifyUI();
            }
        } else {
            mLogisticsInfoActivity.notifyUI();
        }
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return LogisticsInfoItemView_.build(parent.getContext());
    }
}
