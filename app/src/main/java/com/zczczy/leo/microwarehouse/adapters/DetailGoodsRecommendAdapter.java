package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.GoodsDetailRecommendItemView_;
import com.zczczy.leo.microwarehouse.items.GoodsHorizontalItemView_;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by leo on 2016/6/18.
 */
@EBean
public class DetailGoodsRecommendAdapter extends BaseRecyclerViewAdapter<GoodsModel> {

    @Bean
    OttoBus bus;

    @Override
    public void getMoreData(Object... objects) {
        BaseModelJson<List<GoodsModel>> result = new BaseModelJson<>();
        result.Data = (List<GoodsModel>) objects[0];
        result.Successful = true;
        afterGetData(result);
    }

    @UiThread
    void afterGetData(BaseModelJson<List<GoodsModel>> result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            result = new BaseModelJson<>();
//            AndroidTool.showToast(context, no_net);
        } else if (result.Successful) {
            if (result.Data != null && result.Data.size() > 0) {
                insertAll(result.Data, getItems().size());
                bus.post(result);
            }
        }
    }


    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return GoodsDetailRecommendItemView_.build(context);
    }
}

