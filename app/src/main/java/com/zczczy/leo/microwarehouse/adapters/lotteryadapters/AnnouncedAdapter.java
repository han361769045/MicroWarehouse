package com.zczczy.leo.microwarehouse.adapters.lotteryadapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.BaseUltimateViewHolder;
import com.zczczy.leo.microwarehouse.items.ItemView;
import com.zczczy.leo.microwarehouse.items.lotteryitems.AnnouncedItem_;
import com.zczczy.leo.microwarehouse.items.lotteryitems.LotteryHomeItem_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.PagerResult;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.EBean;

/**
 * Created by zczczy on 2016/11/17.
 */
@EBean
public class AnnouncedAdapter extends LotteryUltimateRecyclerViewAdapter<GoodsModel> {
    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        BaseModelJson<PagerResult<GoodsModel>> result = null;
        switch (Integer.valueOf(objects[0].toString())) {
            case 0:
                result = myRestClient.getGoodsInfoLikeWord(pageIndex, pageSize,
                        (objects[1] == null || "".equals(objects[1])) ? "" : objects[1].toString(),
                        objects[2].toString(),
                        (objects[3] == null || "".equals(objects[3])) ? "0" : objects[3].toString(),
                        (objects[4] == null || "".equals(objects[4])) ? "0" : objects[4].toString(),
                        Constants.DEALER.equals(pre.userType().get()) ? "1" : "0",
                        (objects[5] == null || "".equals(objects[5])) ? "" : objects[5].toString()
                );


                break;
            case 1:
                result = myRestClient.getRecommendedGoods(pageIndex, pageSize);
                break;
        }
        afterGetMoreData(result);
    }


    @Override
    void onBindHeaderViewHolder(BaseUltimateViewHolder viewHolder) {

    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
        ItemView<GoodsModel> itemView = null;
        switch (verticalAndHorizontal) {
            case Horizontal:
                itemView = AnnouncedItem_.build(context);
                break;
            case Vertical:
                itemView = AnnouncedItem_.build(context);
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
