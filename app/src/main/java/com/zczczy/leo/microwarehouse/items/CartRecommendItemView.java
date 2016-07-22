package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.CartAdapter;
import com.zczczy.leo.microwarehouse.viewgroup.FullyLinearLayoutManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

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

    @Bean(CartAdapter.class)
    BaseRecyclerViewAdapter myAdapter;

    public CartRecommendItemView(Context context) {
        super(context);

    }

    @Override
    protected void init(Object... objects) {

    }

    @AfterViews
    void afterView() {
//        linearLayoutManager = new FullyLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(myAdapter);
//        myAdapter.getMoreData();
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
