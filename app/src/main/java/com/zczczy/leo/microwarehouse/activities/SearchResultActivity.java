package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.GoodsAdapter;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/21.
 */
@EActivity(R.layout.activity_search_result)
public class SearchResultActivity extends BaseUltimateRecyclerViewActivity<GoodsModel> {


    @Extra
    String searchContent;

    @ViewById
    TextView text_search;

    @ViewById
    RadioButton rb_price;

    boolean isSelected;


    String orderBy = Constants.ZONG_HE;

    @Bean
    void myAdapter(GoodsAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @AfterViews
    void afterView() {
        text_search.setText(searchContent);
        empty_view.setText(empty_search);
        myAdapter.setOnItemClickListener(new BaseUltimateRecyclerViewAdapter.OnItemClickListener<GoodsModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, GoodsModel obj, int position) {
                GoodsDetailActivity_.intent(SearchResultActivity.this).goodsId(obj.GoodsInfoId).start();

            }

            @Override
            public void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });
        myTitleBar.setCustomViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myTitleBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        isSelected = true;
    }

    @CheckedChange
    void rb_filter(boolean isChecked) {
        if (isChecked) {
            orderBy = Constants.ZONG_HE;
            isRefresh = true;
            afterLoadMore();
        }
    }

    @CheckedChange
    void rb_sell_count(boolean isChecked) {
        if (isChecked) {
            orderBy = Constants.SELL_COUNT;
            isRefresh = true;
            afterLoadMore();
        }
    }

    @Click
    void rb_price() {
        if (rb_price.isChecked() && isSelected) {
            isRefresh = true;
            isSelected = false;
            rb_price.setSelected(isSelected);
            orderBy = Constants.PRICE_ASC;
            afterLoadMore();

        } else if (rb_price.isChecked() && !isSelected) {
            isRefresh = true;
            isSelected = true;
            rb_price.setSelected(isSelected);
            orderBy = Constants.PRICE_DESC;
            afterLoadMore();
        }
    }


    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh, 0, searchContent, orderBy);
    }


    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
