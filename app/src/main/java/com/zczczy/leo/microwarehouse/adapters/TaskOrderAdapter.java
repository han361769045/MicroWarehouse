package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.BaseViewHolder;
import com.zczczy.leo.microwarehouse.items.TaskOrderItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.PagerResult;
import com.zczczy.leo.microwarehouse.model.TaskOrderModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.EBean;

/**
 * @author Created by LuLeo on 2016/6/17.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/17.
 */
@EBean
public class TaskOrderAdapter extends BaseUltimateRecyclerViewAdapter<TaskOrderModel> {


    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        BaseModelJson<PagerResult<TaskOrderModel>> bmj = null;
        switch (objects[0].toString()) {
            case "0":
                bmj = myRestClient.getTaskOrderList(pageIndex, pageSize);
                break;
            case "1":
                myRestClient.setHeader("Token", pre.token().get());
                myRestClient.setHeader("Kbn", Constants.ANDROID);
                bmj = myRestClient.getMyTaskOrderList(pageIndex, pageSize, objects[1].toString());
        }
        afterGetMoreData(bmj);
    }


    @Override
    void onBindHeaderViewHolder(BaseViewHolder viewHolder) {

    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
        return TaskOrderItemView_.build(parent.getContext());
    }
}
