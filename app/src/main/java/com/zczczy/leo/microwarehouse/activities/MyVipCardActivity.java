package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.RecyclerView;


import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;



import com.zczczy.leo.microwarehouse.adapters.MyVipCardAdapter;
import com.zczczy.leo.microwarehouse.model.MemberCardDetailInfo;


import org.androidannotations.annotations.AfterViews;

import org.androidannotations.annotations.Bean;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;


/**
 * Created by zczczy on 2016/9/21.
 */
@EActivity(R.layout.activity_vipcard_layout)
public class MyVipCardActivity extends  BaseRecyclerViewActivity<MemberCardDetailInfo> {

    @Extra
    String goodsId;

    @Bean
    void myAdapter(MyVipCardAdapter myAdapter){
        this.myAdapter=myAdapter;

    }

    @AfterViews
    void afterView(){

        myAdapter.getMoreData(goodsId);
        myAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<MemberCardDetailInfo>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, MemberCardDetailInfo obj, int position) {
                VipCardDetailActivity_.intent(MyVipCardActivity.this).goodsId(obj.GoodsInfoId).cardId(obj.MyCardInfoId).start();
            }
        });
    }




}
