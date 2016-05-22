package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.CityItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.CityModel;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by Leo on 2016/5/4.
 */
@EBean
public class CityAdapter extends BaseRecyclerViewAdapter<CityModel> {


    @Override
    public void getMoreData(Object... objects) {
        afterGetData(myRestClient.getCityListByProvinceId(objects[0].toString()));
    }

    @UiThread
    void afterGetData(BaseModelJson<List<CityModel>> bmj) {
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
        return CityItemView_.build(parent.getContext());
    }
}
