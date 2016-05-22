package com.zczczy.leo.microwarehouse.activities;

import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterViews;
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

    @AfterViews
    void afterRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
        paint.setStrokeWidth(1);
        paint.setColor(line_color);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).margin(0).paint(paint).build());
    }


}
