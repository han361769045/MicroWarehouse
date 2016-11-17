package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;


import com.marshalchen.ultimaterecyclerview.CustomUltimateRecyclerview;

import com.marshalchen.ultimaterecyclerview.uiUtils.BasicGridLayoutManager;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseVipCardAdapter;
import com.zczczy.leo.microwarehouse.adapters.EggVipAdapter;


import com.zczczy.leo.microwarehouse.model.MemberCardInfoModel;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.tools.SpacesItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by zczczy on 2016/9/21.
 * 我的服务
 */
@EActivity(R.layout.activity_egg_layout)
public class EggVipActivity extends BaseVipCardActivity<MemberCardInfoModel> {



    @ViewById
    CustomUltimateRecyclerview ultimate_recycler_view;

    BasicGridLayoutManager gridLayoutManager;

    MaterialHeader materialHeader;

    SpacesItemDecoration spacesItemDecoration;




    @Bean
    void myAdapter(EggVipAdapter myAdapter){
        this.myAdapter=myAdapter;

    }


    @AfterViews
    void afterView(){
        ultimate_recycler_view.setHasFixedSize(true);
        gridLayoutManager = new BasicGridLayoutManager(this, 2, OrientationHelper.VERTICAL, false, myAdapter);
        ultimate_recycler_view.setLayoutManager(gridLayoutManager);
        ultimate_recycler_view.setAdapter(myAdapter);
        ultimate_recycler_view.enableLoadmore();
        myAdapter.setOnItemClickListener(new BaseVipCardAdapter.OnItemClickListener<MemberCardInfoModel>() {


            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, MemberCardInfoModel obj, int position) {
                MyVipCardActivity_.intent(EggVipActivity.this).goodsId(obj.ServiceCardId).start();
            }

            @Override
            public void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });
        // refreshingMaterial();
    }

//    void refreshingMaterial() {
//        materialHeader = new MaterialHeader(this);
//        int[] colors = getResources().getIntArray(R.array.google_colors);
//        materialHeader.setColorSchemeColors(colors);
//        materialHeader.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
//        materialHeader.setPadding(0, 15, 0, 10);
//        materialHeader.setPtrFrameLayout(ultimate_recycler_view.mPtrFrameLayout);
//        ultimate_recycler_view.mPtrFrameLayout.autoRefresh(false);
//        ultimate_recycler_view.mPtrFrameLayout.setHeaderView(materialHeader);
//        ultimate_recycler_view.mPtrFrameLayout.addPtrUIHandler(materialHeader);
//        ultimate_recycler_view.mPtrFrameLayout.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                boolean g = PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//                if (g) {
//                    myTitleBar.setVisibility(View.GONE);
//                } else {
//                    myTitleBar.setVisibility(View.VISIBLE);
//                }
//                return g;
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                isRefresh = true;
//                pageIndex = 1;
//                afterLoadMore();
//            }
//        });
//    }






    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh, 1);
    }
}
