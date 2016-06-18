package com.zczczy.leo.microwarehouse.fragments;

import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.DetailGoodsCommentsAdapter;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsCommentsModel;
import com.zczczy.leo.microwarehouse.model.PagerResult;

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
public class GoodsCommentsFragment extends BaseRecyclerViewFragment<GoodsCommentsModel> {

    @Bean
    OttoBus bus;

    @ViewById
    LinearLayout parent;

    @FragmentArg
    String goodsId;

    @AfterViews
    void afterView() {
        bus.register(this);
        afterLoadMore();
    }

    @Bean
    void setMyAdapter(DetailGoodsCommentsAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @Subscribe
    public void notifyUI(BaseModelJson<PagerResult<GoodsCommentsModel>> bmj) {
        float density = getActivity().getResources().getDisplayMetrics().density;
        float viewHeight = 55 * density;
        parent.getLayoutParams().height = (int) viewHeight * 10;
    }


    void afterLoadMore() {
        myAdapter.getMoreData(goodsId);
    }
}
