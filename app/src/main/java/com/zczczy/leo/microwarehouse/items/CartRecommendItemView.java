package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.VerticalDividerItemDecoration;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.CartRecommendGoodsAdapter;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.viewgroup.FullyLinearLayoutManager;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

/**
 * @author Created by LuLeo on 2016/7/22.
 *         you can contact me at :361769045@qq.com
 * @since 2016/7/22.
 */

@EViewGroup(R.layout.cart_recommend)
public class CartRecommendItemView extends ItemView {

    @ViewById
    RecyclerView recyclerView;

    FullyLinearLayoutManager linearLayoutManager;

    @Bean(CartRecommendGoodsAdapter.class)
    BaseRecyclerViewAdapter<GoodsModel> myAdapter;

    @ColorRes
    int line_color;

    public CartRecommendItemView(Context context) {
        super(context);
    }

    Paint paint = new Paint();

    @Override
    protected void init(Object... objects) {
        linearLayoutManager = new FullyLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        paint.setStrokeWidth(1);
        paint.setColor(line_color);
        recyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(context).margin(21).paint(paint).build());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
        myAdapter.getMoreData();
        myAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<GoodsModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, GoodsModel obj, int position) {
                GoodsDetailActivity_.intent(context).goodsId(obj.GoodsInfoId).start();
            }
        });
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
