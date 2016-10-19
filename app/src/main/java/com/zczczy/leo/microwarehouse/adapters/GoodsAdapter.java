package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.BaseViewHolder;
import com.zczczy.leo.microwarehouse.items.GoodsHorizontalItemView_;
import com.zczczy.leo.microwarehouse.items.GoodsVerticalItemView_;
import com.zczczy.leo.microwarehouse.items.ItemView;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.PagerResult;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.EBean;

/**
 * Created by Leo on 2016/5/21.
 */
@EBean
public class GoodsAdapter extends BaseUltimateRecyclerViewAdapter<GoodsModel> {


    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        BaseModelJson<PagerResult<GoodsModel>> result = null;
        switch (Integer.valueOf(objects[0].toString())) {
            case 0:
                result = myRestClient.getGoodsInfoLikeWord(pageIndex, pageSize,
                        objects[1].toString(), objects[2].toString(),
                        (objects[3] == null || "".equals(objects[3])) ? "0" : objects[3].toString(),
                        (objects[4] == null || "".equals(objects[4])) ? "0" : objects[4].toString(),
                        Constants.DEALER.equals(pre.userType().get()) ? "1" : "0");
                break;
            case 1:
                result = myRestClient.getRecommendedGoods(pageIndex, pageSize);
                break;
        }
        afterGetMoreData(result);
    }

    @Override
    void onBindHeaderViewHolder(BaseViewHolder viewHolder) {

    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
        ItemView<GoodsModel> itemView = null;
        switch (verticalAndHorizontal) {
            case Horizontal:
                itemView = GoodsHorizontalItemView_.build(context);
                break;
            case Vertical:
                itemView = GoodsVerticalItemView_.build(context);
                break;
        }
        return itemView;
    }
}
