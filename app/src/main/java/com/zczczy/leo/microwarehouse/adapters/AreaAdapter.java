package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.AreaItemView_;
import com.zczczy.leo.microwarehouse.model.AreaModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by Leo on 2016/5/4.
 */
@EBean
public class AreaAdapter extends BaseRecyclerViewAdapter<AreaModel> {


    @Override
    public void getMoreData(Object... objects) {
        afterGetData(myRestClient.getAreaListByCityId(objects[0].toString()));
    }

    @UiThread
    void afterGetData(BaseModelJson<List<AreaModel>> bmj) {
        if (bmj == null) {
            bmj = new BaseModelJson<>();
//            AndroidTool.showToast(context, no_net);
        } else if (bmj.Successful) {
            if (bmj.Data.size() > 0) {
                insertAll(bmj.Data, getItems().size());
            }
        }

    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return AreaItemView_.build(parent.getContext());
    }
}
