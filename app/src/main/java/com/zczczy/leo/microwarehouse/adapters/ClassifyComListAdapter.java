package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.ClassifyComListItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJsonDoubleT;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;
import com.zczczy.leo.microwarehouse.model.RecommendComModel;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by zhangyan on 2016/10/12.
 * 分类页面 商品列表适配器
 */
@EBean
public class ClassifyComListAdapter extends BaseRecyclerViewAdapter<GoodsTypeModel> {

    @Override
    public void getMoreData(Object... objects) {
        refreshUi((BaseModelJsonDoubleT<List<GoodsTypeModel>, List<RecommendComModel>>) objects[0]);
    }

    @UiThread
    void refreshUi(BaseModelJsonDoubleT<List<GoodsTypeModel>, List<RecommendComModel>> model) {
        AndroidTool.dismissLoadDialog();
        if (model.Successful) {
            insertAll(model.Data, getItems().size());
        } else {
            AndroidTool.showToast(context, model.Error);
        }
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return ClassifyComListItemView_.build(context);
    }

}
