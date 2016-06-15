package com.zczczy.leo.microwarehouse.activities;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.DepotAdapter;
import com.zczczy.leo.microwarehouse.model.DepotModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.res.StringRes;

/**
 * @author Created by LuLeo on 2016/6/15.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/15.
 */
@EActivity(R.layout.activity_ultimate_recycler_view)
public class DepotActivity extends BaseUltimateRecyclerViewActivity<DepotModel> {


    @StringRes
    String text_three;

    @AfterViews
    void afterView() {
        myTitleBar.setTitle(text_three);
    }

    @Bean
    void setMyAdapter(DepotAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh);
    }
}
