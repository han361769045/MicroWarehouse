package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.BaseViewHolder;
import com.zczczy.leo.microwarehouse.items.CommonCategoryHorizontalItemView_;
import com.zczczy.leo.microwarehouse.items.CommonCategoryVerticalItemView_;
import com.zczczy.leo.microwarehouse.items.ItemView;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.EBean;

/**
 * Created by leo on 2016/5/4.
 */
@EBean
public class CommonCategoryAdapter extends BaseUltimateRecyclerViewAdapter<GoodsModel> {


    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        afterGetMoreData(myRestClient.getGoodsInfoByTypeId(
                pageIndex, pageSize, objects[0].toString(),
                objects[1].toString(),
                (objects[2] == null || "".equals(objects[2])) ? "0" : objects[2].toString(),
                (objects[3] == null || "".equals(objects[3])) ? "0" : objects[3].toString(),
                Constants.DEALER.equals(pre.userType().get()) ? "1" : "0")
        );
    }

    @Override
    void onBindHeaderViewHolder(BaseViewHolder viewHolder) {

    }


    @Override
    protected View onCreateItemView(ViewGroup parent) {
        ItemView<GoodsModel> itemView = null;
        switch (verticalAndHorizontal) {
            case Horizontal:
                itemView = CommonCategoryHorizontalItemView_.build(parent.getContext());
                break;
            case Vertical:
                itemView = CommonCategoryVerticalItemView_.build(parent.getContext());
                break;
        }
        return itemView;
    }

}
