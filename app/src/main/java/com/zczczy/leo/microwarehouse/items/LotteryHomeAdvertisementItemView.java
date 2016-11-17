package com.zczczy.leo.microwarehouse.items;

import android.content.Context;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;


import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.R;

import com.zczczy.leo.microwarehouse.activities.ClassifyActivity_;
import com.zczczy.leo.microwarehouse.activities.CommonWebViewActivity_;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;
import com.zczczy.leo.microwarehouse.activities.SearchResultActivity_;
import com.zczczy.leo.microwarehouse.activities.lotteryactivities.AnnouncedActivity_;

import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.model.BannerModel;


import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;


import com.zczczy.leo.microwarehouse.viewgroup.MarqueeView;
import com.zczczy.leo.microwarehouse.views.GlideSliderView;


import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by zczczy on 2016/9/29.
 */
@EViewGroup(R.layout.item_home_herader)
public class LotteryHomeAdvertisementItemView extends ItemView<List<AdvertModel>>implements BaseSliderView.OnSliderClickListener  {

    @ViewById
    SliderLayout home_slider;
    @ViewById
    MarqueeView marqueeView;

    @ViewById
    ImageView ad_one;
    @ViewById
    TextView txt_one,txt_two,txt_three,txt_four;

    @Bean
    MyBackgroundTask myBackgroundTask;

    @ViewById
    LinearLayout ll_to_announced;


    @App
    MyApplication app;


    public LotteryHomeAdvertisementItemView(Context context) {
        super(context);
    }

    @UiThread
    void afterGetData() {
        init();
    }

    @Override
    protected void init(Object... objects) {

        List<String> info = new ArrayList<>();
        info.add("恭喜中奖123456");
        info.add("恭喜中奖654321");
        info.add("恭喜中奖aaaaaa");
        info.add("恭喜中奖bbbbbb");
        info.add("恭喜中奖cccccc");
        info.add("恭喜中奖dddddd");
        marqueeView.startWithList(info);

        home_slider.removeAllSliders();
        for (BannerModel bannerModel:app.getNewBannerList()){
            //显示 描述和图片sliderView
            GlideSliderView textSliderView = new GlideSliderView(context);
            textSliderView.image(bannerModel.BannerImgUrl);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bannerModel", bannerModel);
            textSliderView.bundle(bundle);
            textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
            textSliderView.setOnSliderClickListener(this);
            home_slider.addSlider(textSliderView);
        }
        for (final  AdvertModel advertModel:app.getAdvertModelList()){
            if (27==advertModel.AdsenseTypeId){
                Glide.with(context)
                        .load(advertModel.AdvertImg)
                        .skipMemoryCache(true)
                        .crossFade()
                        .error(R.drawable.goods_default)
                        .placeholder(R.drawable.goods_default)
                        .into(ad_one);

                ad_one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (1==advertModel.JumpType) {
                            // CategoryActivity_.intent(getActivity()).id(temp[1]).title(temp[2]).start();
                            ClassifyActivity_.intent(context).id(advertModel.InfoId).start();
                        }
                        else  if(2==advertModel.JumpType){
                            GoodsDetailActivity_.intent(context).goodsId(advertModel.InfoId).start();
                        }
                        else if(3==advertModel.JumpType){
                            SearchResultActivity_.intent(context).IsAppointmentPro("1").searchContent("").start();
                        }
                        else {
                            CommonWebViewActivity_.intent(context).linkUrl(advertModel.InfoId).title(advertModel.AdvertName).start();

                        }
                    }
                });
            }
        }

    }

    @Click
    void txt_one(){
        AnnouncedActivity_.intent(context).start();
    }



    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }

    public void stopAutoCycle() {
        home_slider.stopAutoCycle();

    }

    public void startAutoCycle() {
        home_slider.startAutoCycle();

    }


    // banner点击
    @Override
    public void onSliderClick(BaseSliderView slider) {

    }




}
