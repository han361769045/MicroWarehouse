package com.zczczy.leo.microwarehouse.activities;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zczczy.leo.microwarehouse.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zczczy on 2016/10/14.
 * 有机鸡蛋
 */
@EActivity(R.layout.activity_organic_layout)
public class OrganicEggActivity extends BaseActivity {
    @ViewById
    ImageView img_one,img_two,img_three,img_four,img_five,img_six,gif_one,gif_two,gif_three;


    @AfterViews
    void afterView(){

        Glide.with(this)
                .load(R.drawable.green_egg_gif)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .placeholder(R.drawable.goods_default)
                .error(R.drawable.goods_default)
                .into(gif_one);
        Glide.with(this)
                .load(R.drawable.blue_zt_gif)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .placeholder(R.drawable.goods_default)
                .error(R.drawable.goods_default)
                .into(gif_two);

        Glide.with(this)
                .load(R.drawable.red_zt_gif)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .placeholder(R.drawable.goods_default)
                .error(R.drawable.goods_default)
                .into(gif_three);

    }

    @Click
    void img_four(){
        GoodsDetailActivity_.intent(this).goodsId("b08907dbd7d44feda8405b8fbcc0cb12").start();
    }

    @Click
    void img_five(){
        GoodsDetailActivity_.intent(this).goodsId("3f497e96f9fd43d592e65889fd59f143").start();
    }
    @Click
    void img_six(){
        GoodsDetailActivity_.intent(this).goodsId("fcb85e7334f94c25b4be2a448d0e0092").start();
    }
}
