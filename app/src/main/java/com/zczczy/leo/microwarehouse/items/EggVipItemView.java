package com.zczczy.leo.microwarehouse.items;

import android.content.Context;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;

import com.zczczy.leo.microwarehouse.model.MemberCardInfoModel;

import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;


import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;

import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by zczczy on 2016/9/21.
 */
@EViewGroup(R.layout.item_eggvip)
public class EggVipItemView extends ItemView<MemberCardInfoModel> {
    @ViewById
    ImageView img_pic;

    @ViewById
    View view_buy,view_notbuy;

    @Pref
    MyPrefs_ pre;

    @ViewById
    LinearLayout ll_root;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;




    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }


    public EggVipItemView(Context context) {
        super(context);
    }


    @Override
    protected void init(Object... objects) {


        if (!StringUtils.isEmpty(_data.CardImgUrl)) {
            Glide.with(context)
                    .load(_data.CardImgUrl)
                    .skipMemoryCache(true)
                    .crossFade()
                    .centerCrop()
                    .placeholder(R.drawable.goods_default)
                    .bitmapTransform(new RoundedCornersTransformation(context,5,0))
                    .error(R.drawable.goods_default)
                    .into(img_pic);
            if (_data.Kbn==2){
                view_buy.setVisibility(VISIBLE);
                view_buy.setBackgroundResource(R.drawable.notbuy);
                view_buy.bringToFront();
                view_notbuy.setBackgroundResource(R.drawable.view_notbuy_bg);
                view_notbuy.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoodsDetailActivity_.intent(context).goodsId(_data.GoodsInfoId).start();
                    }
                });

            }
            if (_data.Kbn==3){
                view_buy.setVisibility(VISIBLE);
                view_buy.bringToFront();
                view_notbuy.setBackgroundResource(R.drawable.view_notbuy_bg);
                view_buy.setBackgroundResource(R.drawable.outofdate);
                view_notbuy.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoodsDetailActivity_.intent(context).goodsId(_data.GoodsInfoId).start();
                    }
                });
            }

            if (_data.Kbn==4){
                view_buy.setVisibility(VISIBLE);
                view_buy.bringToFront();
                view_notbuy.setBackgroundResource(R.drawable.view_notbuy_bg);
                view_buy.setBackgroundResource(R.drawable.pleaseexpect);
                view_notbuy.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AndroidTool.showToast(context,"敬请期待");
                    }
                });
            }

        }


    }






    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
