package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;


import com.zczczy.leo.microwarehouse.activities.CategoryActivity;
import com.zczczy.leo.microwarehouse.items.CategoryItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJsonDoubleT;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;
import com.zczczy.leo.microwarehouse.model.RecommendComModel;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by Leo on 2016/5/4.
 */
@EBean
public class CategoryAdapter extends BaseRecyclerViewAdapter<GoodsTypeModel> {

    @RootContext
    CategoryActivity categoryActivity;

    @Override
    public void getMoreData(Object... objects) {
        BaseModelJsonDoubleT<List<GoodsTypeModel>, List<RecommendComModel>> bmj = myRestClient.getGoodsTypeListByPid(objects[0].toString());
        afterGetData(bmj);
    }

    @UiThread
    void afterGetData(BaseModelJsonDoubleT<List<GoodsTypeModel>, List<RecommendComModel>> bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            bmj = new BaseModelJsonDoubleT<>();
//            AndroidTool.showToast(context, no_net);
        } else if (bmj.Successful) {

            if (bmj.Data.size() > 0) {
                insertAll(bmj.Data, getItems().size());
            }
        }
//        categoryActivity.notifyUI(bmj);
    }


    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return CategoryItemView_.build(parent.getContext());
    }




}