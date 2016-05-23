package com.zczczy.leo.microwarehouse.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.BaseUltimateViewHolder;
import com.zczczy.leo.microwarehouse.items.GoodsHorizontalItemView_;
import com.zczczy.leo.microwarehouse.items.GoodsVerticalItemView_;
import com.zczczy.leo.microwarehouse.items.ItemView;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.PagerResult;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;

/**
 * Created by Leo on 2016/5/21.
 */
@EBean
public class GoodsAdapter extends BaseUltimateRecyclerViewAdapter<GoodsModel> {


    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        BaseModelJson<PagerResult<GoodsModel>> result = new BaseModelJson<>();

//        switch (Integer.valueOf(objects[0].toString())) {
//            case 0:
//                result = myRestClient.getGoodsInfoLikeWord(pageIndex, pageSize, objects[1].toString(), objects[2].toString());
//                break;
//        }
        result.Successful = true;
        result.Data = new PagerResult<>();
        result.Data.ListData = new ArrayList<>();
        result.Data.RowCount = 91;
        for (int i = pageSize * (pageIndex - 1); i < pageSize * pageIndex; i++) {
            GoodsModel goodsModel = new GoodsModel();
            goodsModel.GodosName = "巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉巴拉" + i;
            goodsModel.GoodsPrice = (i + 1);
            result.Data.ListData.add(goodsModel);
        }
        afterGetMoreData(result);

    }

    @UiThread
    void afterGetMoreData(BaseModelJson<PagerResult<GoodsModel>> result) {
        if (result == null) {
            result = new BaseModelJson<>();
        } else if (!result.Successful) {
            AndroidTool.showToast(context, result.Error);
        } else {
            if (isRefresh) {
                clear();
            }
            setTotal(result.Data.RowCount);
            if (result.Data.ListData.size() > 0) {
                insertAll(result.Data.ListData, getItems().size());
            }
        }
        bus.post(result);
    }


    @Override
    void onBindHeaderViewHolder(BaseUltimateViewHolder viewHolder) {

    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
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

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
