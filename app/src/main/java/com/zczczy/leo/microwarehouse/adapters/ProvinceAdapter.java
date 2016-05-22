package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.ProvinceItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.ProvinceModel;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;

/**
 * Created by Leo on 2016/5/4.
 */
@EBean
public class ProvinceAdapter extends BaseRecyclerViewAdapter<ProvinceModel> {

    @StringRes
    String no_net;

    @Override
    public void getMoreData(Object... objects) {
        afterGetData(myRestClient.getAllProvinceList());
    }

    @UiThread
    void afterGetData(BaseModelJson<List<ProvinceModel>> bmj) {
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
        return ProvinceItemView_.build(parent.getContext());
    }
}
