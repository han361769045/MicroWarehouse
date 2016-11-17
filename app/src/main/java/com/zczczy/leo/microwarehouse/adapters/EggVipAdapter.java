package com.zczczy.leo.microwarehouse.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.BaseUltimateViewHolder;
import com.zczczy.leo.microwarehouse.items.EggVipItemView_;


import com.zczczy.leo.microwarehouse.model.BaseModelJson;

import com.zczczy.leo.microwarehouse.model.MemberCardInfoModel;

import com.zczczy.leo.microwarehouse.tools.Constants;


import org.androidannotations.annotations.EBean;

import java.util.List;


/**
 * Created by zczczy on 2016/9/21.
 * 我的服务
 */
@EBean
public class EggVipAdapter extends BaseVipCardAdapter<MemberCardInfoModel> {


    @Override
    public void getMoreData(Object... objects) {
        BaseModelJson<List<MemberCardInfoModel>> result;
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        result =myRestClient.GetServiceCardInfo();
        afterGetMoreData(result);
    }


    @Override
    void onBindHeaderViewHolder(BaseUltimateViewHolder viewHolder) {

    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {

    return EggVipItemView_.build(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
