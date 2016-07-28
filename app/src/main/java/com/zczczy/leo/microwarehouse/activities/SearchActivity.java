package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.HotSearchAdapter;
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

    @ViewById
    public LinearLayout ll_hot;

    @ViewById
    RecyclerView recycler_view_hot;

    SearchHistory searchHistory;

    @Bean
    HotSearchAdapter hotSearchAdapter;

    LinearLayoutManager hLinearLayoutManager;

    @Bean
    void setMyAdapter(SearchHistoryAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }


    @AfterViews
    void afterView() {

        setHotSearch();
        myAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<SearchHistory>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, SearchHistory obj, int position) {
                searchHistoryDao.update(obj);
                SearchResultActivity_.intent(SearchActivity.this).searchContent(obj.getSearchContent()).startForResult(1000);
            }
        });
        myAdapter.getMoreData(0, 0);

        myTitleBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    void setHotSearch() {
        hLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_hot.setLayoutManager(hLinearLayoutManager);
        recycler_view_hot.setAdapter(hotSearchAdapter);
        hotSearchAdapter.getMoreData();
        hotSearchAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, String obj, int position) {
                text_search.setText(obj);
                search();
            }
        });
    }


    @EditorAction
    void text_search(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
        }
    }

    void search() {
        if (!AndroidTool.checkIsNull(text_search)) {
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
