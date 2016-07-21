package com.luleo.baselibrary.activities;

import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.luleo.baselibrary.adapters.BaseRecyclerViewAdapter;
import com.luleo.baselibrary.rest.MyErrorHandler;
import com.luleo.baselibrary.tools.AndroidTool;
import com.luleo.baselibrary.viewgroup.MyTitleBar;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/21.
 */
@EActivity
public abstract class BaseRecyclerViewActivity<T> extends BaseActivity {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;

    BaseRecyclerViewAdapter<T> myAdapter;

    Paint paint = new Paint();

    @Bean
    MyErrorHandler myErrorHandler;


    @AfterViews
    void afterRecyclerView() {
        AndroidTool.showLoadDialog(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
        paint.setStrokeWidth(1);
        paint.setColor(line_color);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).margin(0).paint(paint).build());
    }


}
