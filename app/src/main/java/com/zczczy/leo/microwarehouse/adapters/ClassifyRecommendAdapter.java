package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.RecommendItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJsonDoubleT;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;
import com.zczczy.leo.microwarehouse.model.RecommendComModel;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by zhangyan on 2016/10/12.
 * 分类页面 推荐商品
 */
@EBean
public class ClassifyRecommendAdapter extends BaseRecyclerViewAdapter<RecommendComModel> {
    @Override
    public void getMoreData(Object... objects) {
        refreshUi((BaseModelJsonDoubleT<List<GoodsTypeModel>, List<RecommendComModel>>) objects[0]);
    }

    @UiThread
    void refreshUi(BaseModelJsonDoubleT<List<GoodsTypeModel>, List<RecommendComModel>> model) {
        if (model.Successful) {
            insertAll(model.Data2, getItems().size());
        } else {
            AndroidTool.showToast(context, model.Error);
        }
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return RecommendItemView_.build(context);
    }
}
