package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.ClassifyTypeItemView_;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.ClassifyDataModel;

import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by zhangyan on 2016/10/12.
 * 分类页面 商品分类适配器
 */
@EBean
public class ClassifyTypeAdapter extends BaseRecyclerViewAdapter<ClassifyDataModel> {

    @Bean
    OttoBus bus;

    @Override
    public void getMoreData(Object... objects) {
        refreshUi((BaseModelJson<List<ClassifyDataModel>>) objects[0]);
    }

    @UiThread
    void refreshUi(BaseModelJson<List<ClassifyDataModel>> model) {
        if (model.Successful) {
            insertAll(model.Data, getItems().size());
            bus.post(1002);
        } else {
            AndroidTool.showToast(context, model.Error);
        }
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return ClassifyTypeItemView_.build(context);
    }
}
