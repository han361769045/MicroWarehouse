package com.zczczy.leo.microwarehouse.adapters;



import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.activities.MyVipCardActivity;
import com.zczczy.leo.microwarehouse.items.VipCardItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.MemberCardDetailInfo;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by zczczy on 2016/9/21.
 */
@EBean
public class MyVipCardAdapter extends BaseRecyclerViewAdapter<MemberCardDetailInfo> {

    @RootContext
    MyVipCardActivity myVipCardActivity;

    @Override
    public void getMoreData(Object... objects) {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        BaseModelJson<List<MemberCardDetailInfo>> bmj=myRestClient.GetMyCardInfoByServiceCardId(objects[0].toString());
        afterGetMoreData(bmj);

    }



    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return VipCardItemView_.build(context);
    }
}
