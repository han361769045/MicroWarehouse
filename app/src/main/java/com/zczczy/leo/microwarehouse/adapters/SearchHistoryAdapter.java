package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;


import com.zczczy.leo.microwarehouse.dao.SearchHistory;
import com.zczczy.leo.microwarehouse.dao.SearchHistoryDao;
import com.zczczy.leo.microwarehouse.items.SearchHistoryItemView_;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by leo on 2016/5/5.
 */
@EBean
public class SearchHistoryAdapter extends BaseRecyclerViewAdapter<SearchHistory> {

    @Bean
    SearchHistoryDao searchHistoryDao;


    @Override
    public void getMoreData(Object... objects) {
        afterGetMoreData(searchHistoryDao.getAll());
    }

    @UiThread
    void afterGetMoreData(List<SearchHistory> bmj) {
        AndroidTool.dismissLoadDialog();
        if (getItemCount() > 0) {
            clear();
        }
        insertAll(bmj, getItems().size());
    }


    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return SearchHistoryItemView_.build(parent.getContext());
    }
}
