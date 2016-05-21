package com.zczczy.leo.microwarehouse.activities;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.GoodsAdapter;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Leo on 2016/5/21.
 */
@EActivity(R.layout.activity_search_result)
public class SearchResultActivity extends UltimateRecyclerViewActivity {


    @Bean
    void myAdapter(GoodsAdapter myAdapter) {
        this.myAdapter = myAdapter;

    }


    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh);
    }

    @CheckedChange
    void rb_others(boolean isChecked) {
        if(isChecked){
            verticalItem();
        }

    }

    @CheckedChange
    void rb_sell_count(boolean isChecked) {
        if(isChecked){
            horizontalItem();
        }

    }
}
