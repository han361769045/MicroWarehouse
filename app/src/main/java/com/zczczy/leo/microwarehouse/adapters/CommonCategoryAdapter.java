package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.GoodsHorizontalItemView_;
import com.zczczy.leo.microwarehouse.items.GoodsVerticalItemView_;
import com.zczczy.leo.microwarehouse.items.ItemView;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by leo on 2016/5/4.
 */
@EBean
public class CommonCategoryAdapter extends BaseRecyclerViewAdapter<GoodsModel> {


    @Override
    public void getMoreData(Object... objects) {
        afterGetData(myRestClient.getGoodsInfoByTypeId(objects[0].toString(), objects[1].toString()));
    }

    @UiThread
    void afterGetData(BaseModelJson<List<GoodsModel>> bmj) {
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
        ItemView<GoodsModel> itemView = null;
        switch (verticalAndHorizontal) {
            case Horizontal:
                itemView = GoodsHorizontalItemView_.build(parent.getContext());
                break;
            case Vertical:
                itemView = GoodsVerticalItemView_.build(parent.getContext());
                break;
        }
        return itemView;
    }
}
