package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.SearchHistoryAdapter;
import com.zczczy.leo.microwarehouse.dao.SearchHistory;
import com.zczczy.leo.microwarehouse.dao.SearchHistoryDao;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/23.
 */
@EActivity(R.layout.activity_search)
public class SearchActivity extends BaseRecyclerViewActivity<SearchHistory> {

    @Bean
    SearchHistoryDao searchHistoryDao;

    @ViewById
    EditText text_search;

    SearchHistory searchHistory;

    @Bean
    void setMyAdapter(SearchHistoryAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @AfterViews
    void afterView() {
        myAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<SearchHistory>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, SearchHistory obj, int position) {
                searchHistoryDao.update(obj);
                SearchResultActivity_.intent(SearchActivity.this).searchContent(obj.getSearchContent()).startForResult(1000);
            }
        });
        myAdapter.getMoreData(0, 0);
    }

    @EditorAction
    void text_search(int actionId) {
        if (!AndroidTool.checkIsNull(text_search) && actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchHistory = new SearchHistory();
            searchHistory.setSearchContent(text_search.getText().toString());
            searchHistory.setSearchTime(String.valueOf(System.currentTimeMillis()));
            searchHistoryDao.insert(searchHistory);
            SearchResultActivity_.intent(SearchActivity.this).searchContent(text_search.getText().toString()).startForResult(1000);
        }
    }

    @Click
    void btn_clear() {
        searchHistoryDao.clear();
        myAdapter.getMoreData(0, 0);
    }

    @OnActivityResult(value = 1000)
    void onResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            myAdapter.getMoreData(0, 0);
        }
    }
}
