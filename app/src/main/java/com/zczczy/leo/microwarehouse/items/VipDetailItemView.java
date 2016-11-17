package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.MemberCardDetailInfo;
import com.zczczy.leo.microwarehouse.model.PagerResult;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by zczczy on 2016/9/21.
 * 订单详情header
 */
@EViewGroup(R.layout.item_vipdetail)
public class VipDetailItemView extends ItemView<MemberCardDetailInfo> {
    @ViewById
    ImageView img_vipcard;
    @ViewById
    TextView card_num,buy_time,card_value,card_off,card_already;

    Context context;
    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;
    @AfterInject
    void setMyErrorHandler() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }


    public VipDetailItemView(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void init(Object... objects) {
        if (!StringUtils.isEmpty(_data.CardImgUrl)) {
            Glide.with(context)
                    .load(_data.CardImgUrl)
                    .skipMemoryCache(true)
                    .crossFade()
                    .centerCrop()
                    .bitmapTransform(new RoundedCornersTransformation(context,5,0))
                    .placeholder(R.drawable.goods_default)
                    .error(R.drawable.goods_default)
                    .into(img_vipcard);
        }
        card_num.setText(_data.CardNo);
        buy_time.setText(_data.BuyDateTime);
        card_value.setText(_data.MianZhiInfo);
        card_off.setText(_data.YouHuiInfo);
        card_already.setText(_data.UseCount);


    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
