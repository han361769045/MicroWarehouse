package com.zczczy.leo.microwarehouse.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.provider.FixedLoadProvider;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.FlexibleDividerDecoration;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.VerticalDividerItemDecoration;
import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.DetailGoodsRecommendAdapter;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.tools.DensityUtil;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * @author Created by LuLeo on 2016/6/16.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/16.
 */
@EFragment(R.layout.fragment_goods_comments)
public class GoodsFragment extends BaseRecyclerViewFragment<GoodsModel> {

    @Bean
    OttoBus bus;

    @ViewById
    LinearLayout parent;

    @FragmentArg
    GoodsModel mGoodsModel;

    @AfterInject
    void afterInject() {
        gridCount = 2;
    }

    @AfterViews
    void afterView() {
        horizontalItem();
        itemDecoration = new HorizontalDividerItemDecoration.Builder(getActivity()).margin(21)
                .visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
                    @Override
                    public boolean shouldHideDivider(int position, RecyclerView parent) {
                        return position == 0;
                    }
                }).paint(paint).build();

        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(getActivity())
                .margin(21).visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
                    @Override
                    public boolean shouldHideDivider(int position, RecyclerView parent) {
                        return position == 0;
                    }
                }).paint(paint).build());
        afterLoadMore();
    }

    @Bean
    void setMyAdapter(DetailGoodsRecommendAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @Subscribe
    public void notifyUI(BaseModelJson<List<GoodsModel>> bmj) {
        parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), myAdapter.getItemCount() * 141)));
    }

    void afterLoadMore() {
        myAdapter.getMoreData(mGoodsModel.RecommendedGoodsList);
    }


    @Override
    public void onHiddenChanged(boolean isHidden) {
        if (isHidden) {
            bus.unregister(this);
        } else
            bus.register(this);
    }
}
