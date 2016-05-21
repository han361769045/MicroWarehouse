package com.zczczy.leo.microwarehouse.fragments;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/21.
 */
@EFragment
public abstract class RecyclerViewFragment extends BaseFragment {

    @ViewById
    RecyclerView recyclerView;

    GridLayoutManager gridLayoutManager;

    @AfterViews
    void afterRecyclerView() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
