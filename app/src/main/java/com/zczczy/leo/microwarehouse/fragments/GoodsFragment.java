package com.zczczy.leo.microwarehouse.fragments;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.DetailGoodsCommentsAdapter;
import com.zczczy.leo.microwarehouse.adapters.GoodsAdapter;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsCommentsModel;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.PagerResult;
import com.zczczy.leo.microwarehouse.tools.DensityUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * @author Created by LuLeo on 2016/6/16.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/16.
 */
@EFragment(R.layout.fragment_goods_comments)
public class GoodsFragment extends BaseUltimateRecyclerViewFragment<GoodsModel> {

    @Bean
    OttoBus bus;

    @ViewById
    LinearLayout parent;

    @FragmentArg
    String goodsId;

    @AfterViews
    void afterView() {
        afterLoadMore();
    }

    @Bean
    void setMyAdapter(GoodsAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @Subscribe
    public void notifyUI(BaseModelJson<PagerResult<GoodsCommentsModel>> bmj) {
        parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), myAdapter.getItemCount() * 60)));
    }

    void afterLoadMore() {
//        myAdapter.getMoreData(goodsId);
    }


    @Override
    public void onHiddenChanged(boolean isHidden) {
        if (isHidden) {
            bus.unregister(this);
        } else
            bus.register(this);
    }
}
