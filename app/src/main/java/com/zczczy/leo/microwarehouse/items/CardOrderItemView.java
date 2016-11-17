package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.MemberCardCommentsModel;
import com.zczczy.leo.microwarehouse.model.MemberCardDetailInfo;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import static com.zczczy.leo.microwarehouse.R.string.no_net;

/**
 * Created by zczczy on 2016/9/21.
 * 会员卡订单详情下边列表
 */
@EViewGroup(R.layout.item_cardorder)
public class CardOrderItemView extends ItemView<MemberCardCommentsModel> {

    @ViewById
    ImageView img_vipcard;

    @ViewById
    TextView reserve_time,send_time,sign_time,card_num;

    @Bean
    MyErrorHandler myErrorHandler;
    @RestService
    MyRestClient myRestClient;
    @Pref
    MyPrefs_ pre;


    @AfterInject
    void afterInject(){

        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView(){


    }

    public CardOrderItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {

        if (!StringUtils.isEmpty(_data.DetailImgUrl)) {
            Glide.with(context)
                    .load(_data.DetailImgUrl)
                    .skipMemoryCache(true)
                    .crossFade()
                    .centerCrop()
                    .placeholder(R.drawable.goods_default)
                    .error(R.drawable.goods_default)
                    .into(img_vipcard);
        }
         reserve_time.setText(_data.YdTime);
         send_time.setText(_data.FhTime);
         sign_time.setText(_data.QsTime);
         card_num.setText(_data.BaseCount+"x"+_data.CountVal);
         card_num.setTextColor(Color.RED);


    }



    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
