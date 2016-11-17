package com.zczczy.leo.microwarehouse.adapters;


import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.items.CardOrderItemView_;

import com.zczczy.leo.microwarehouse.model.BaseModelJson;


import com.zczczy.leo.microwarehouse.model.MemberCardCommentsModel;
import com.zczczy.leo.microwarehouse.model.MemberCardDetailInfo;

import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.EBean;

import java.util.List;

/**
 * Created by zczczy on 2016/9/22.
 */
@EBean
public class CardDetailAdapter extends BaseRecyclerViewAdapter<MemberCardCommentsModel> {


    @Override
    public void getMoreData(Object... objects) {
        BaseModelJson<List<MemberCardCommentsModel>> result = new BaseModelJson<>();

        myRestClient.setHeader("Token", pre.token().get());

        myRestClient.setHeader("Kbn", Constants.ANDROID);

        BaseModelJson<MemberCardDetailInfo> bmn = myRestClient.GetMyCardInfoById(objects[0].toString());

        if (bmn != null && bmn.Data != null) {

            result.Data = bmn.Data.CardDetailInfoList;

            result.Successful = true;
        } else {
            result.Error = bmn.Error;
        }
        afterGetMoreData(result);
    }


    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return CardOrderItemView_.build(parent.getContext());
    }
}
