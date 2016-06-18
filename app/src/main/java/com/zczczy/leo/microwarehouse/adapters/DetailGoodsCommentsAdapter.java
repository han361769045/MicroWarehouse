package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.GoodsCommentsItemView_;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsCommentsModel;
import com.zczczy.leo.microwarehouse.model.PagerResult;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

/**
 * Created by leo on 2016/6/18.
 */
@EBean
public class DetailGoodsCommentsAdapter extends BaseRecyclerViewAdapter<GoodsCommentsModel> {

    @Bean
    OttoBus bus;

    @Override
    public void getMoreData(Object... objects) {
        afterGetData(myRestClient.getGoodsCommentsByGoodsInfoId(1, 20, objects[0].toString()));
    }

    @UiThread
    void afterGetData(BaseModelJson<PagerResult<GoodsCommentsModel>> bmj) {
        if (bmj == null) {
            bmj = new BaseModelJson<>();
//            AndroidTool.showToast(context, no_net);
        } else if (bmj.Successful) {
            if (bmj.Data.ListData.size() > 0) {
                insertAll(bmj.Data.ListData, getItems().size());
                bus.post(bmj);
            }
        }
    }


    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return GoodsCommentsItemView_.build(parent.getContext());
    }
}

