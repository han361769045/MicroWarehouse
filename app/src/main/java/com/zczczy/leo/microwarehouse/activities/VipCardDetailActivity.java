package com.zczczy.leo.microwarehouse.activities;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;

import com.zczczy.leo.microwarehouse.adapters.CardDetailAdapter;


import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.MemberCardCommentsModel;
import com.zczczy.leo.microwarehouse.model.MemberCardDetailInfo;
import com.zczczy.leo.microwarehouse.tools.Constants;


import org.androidannotations.annotations.AfterViews;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.StringUtils;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by zczczy on 2016/9/21.
 * 我的会员卡订单详情页
 */
@EActivity(R.layout.activity_vipdetails_layout)
public class VipCardDetailActivity extends BaseRecyclerViewActivity<MemberCardCommentsModel> {
    @Extra
    String cardId,goodsId;

    @ViewById
    ImageView img_vipcard;

    @ViewById
    LinearLayout ll_ll;
    @ViewById
    TextView card_num,buy_time,card_value,card_off,card_already;
    @Bean
    void myAdapter(CardDetailAdapter myAdapter){
        this.myAdapter=myAdapter;

    }


    @AfterViews
    void afterView() {

      getMemberInfo(cardId);
      myAdapter.getMoreData(cardId);

    }

    @Background
    void getMemberInfo(String MyCardInfoId){
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        BaseModelJson<MemberCardDetailInfo> bmj = myRestClient.GetMyCardInfoById(MyCardInfoId);
        afterGetInfo(bmj);

    }

    @UiThread
    void afterGetInfo(BaseModelJson<MemberCardDetailInfo>bmj){
        if (bmj!=null&&bmj.Successful){
            if (!StringUtils.isEmpty(bmj.Data.CardImgUrl)) {
                Glide.with(this)
                        .load(bmj.Data.CardImgUrl)
                        .skipMemoryCache(true)
                        .crossFade()
                        .centerCrop()
                        .bitmapTransform(new RoundedCornersTransformation(this,5,0))
                        .placeholder(R.drawable.goods_default)
                        .error(R.drawable.goods_default)
                        .into(img_vipcard);
            }
            card_num.setText(bmj.Data.CardNo);
            buy_time.setText(bmj.Data.BuyDateTime);
            card_value.setText(bmj.Data.MianZhiInfo);
            card_off.setText(bmj.Data.YouHuiInfo);
            card_already.setText(bmj.Data.UseCount);
        }

    }




    @Click
    void ll_ll(){
        GoodsDetailActivity_.intent(this).goodsId(goodsId).start();
    }

}
