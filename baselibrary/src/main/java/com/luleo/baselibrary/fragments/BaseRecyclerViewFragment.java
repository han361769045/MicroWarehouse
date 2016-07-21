package com.luleo.baselibrary.fragments;

import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.luleo.baselibrary.adapters.BaseRecyclerViewAdapter;
import com.luleo.baselibrary.rest.MyErrorHandler;
import com.luleo.baselibrary.tools.AndroidTool;
import com.luleo.baselibrary.viewgroup.MyTitleBar;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/21.
 */
@EFragment
public abstract class BaseRecyclerViewFragment<T> extends BaseFragment {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    RecyclerView recyclerView;

    @Bean
    MyErrorHandler myErrorHandler;

    BaseRecyclerViewAdapter<T> myAdapter;

    GridLayoutManager gridLayoutManager;

    LinearLayoutManager linearLayoutManager;

    Paint paint = new Paint();


    @AfterViews
    void afterRecyclerView() {
        AndroidTool.showLoadDialog(this);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        paint.setStrokeWidth(1);
        paint.setColor(line_color);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).margin(35).paint(paint).build());
        verticalItem();
    }

    //线性布局
    void verticalItem() {
        recyclerView.setAdapter(null);
        myAdapter.verticalAndHorizontal = BaseRecyclerViewAdapter.VerticalAndHorizontal.Vertical;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    //网格布局
    void horizontalItem() {
        recyclerView.setAdapter(null);
        myAdapter.verticalAndHorizontal = BaseRecyclerViewAdapter.VerticalAndHorizontal.Horizontal;
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }

}
