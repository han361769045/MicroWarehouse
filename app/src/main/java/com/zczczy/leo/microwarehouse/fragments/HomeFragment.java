package com.zczczy.leo.microwarehouse.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.CategoryActivity_;
import com.zczczy.leo.microwarehouse.activities.ClassifyActivity_;
import com.zczczy.leo.microwarehouse.activities.CommonWebViewActivity_;
import com.zczczy.leo.microwarehouse.activities.DepotActivity_;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;

import com.zczczy.leo.microwarehouse.activities.OrganicEggActivity_;
import com.zczczy.leo.microwarehouse.activities.SearchActivity_;
import com.zczczy.leo.microwarehouse.activities.SearchResultActivity_;
import com.zczczy.leo.microwarehouse.activities.TaskOrderActivity_;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.model.BannerModel;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.NoticeInfoModel;
import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.service.LocationService;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import com.zczczy.leo.microwarehouse.viewgroup.GradationScrollView;
import com.zczczy.leo.microwarehouse.viewgroup.HornSliderView;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;
import com.zczczy.leo.microwarehouse.views.GlideSliderView;
import com.zczczy.leo.microwarehouse.views.LoopViewPager;
import com.zczczy.leo.microwarehouse.views.ScalePagerTransformer;
import com.zczczy.leo.microwarehouse.views.WelfareAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import org.androidannotations.annotations.res.AnimationRes;
import org.androidannotations.annotations.res.DrawableRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

/**
 * Created by Leo on 2016/5/20.
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener, BDLocationListener,GradationScrollView.ScrollViewListener {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    LinearLayout second_title_bar;

    @ViewById
    ImageView ad_newimg;

    @ViewById
    RelativeLayout rl_search_two;

    private WelfareAdapter adapter;

    @RestService
    MyRestClient myRestClient;

    @ViewById
    RelativeLayout activity_test;

    @ViewById
    LoopViewPager banner_Vp;

    @ViewById
    SliderLayout homeSlider, slider_horn;

    @Bean
    MyBackgroundTask myBackgroundTask;

    @ViewById
    TextView txt_one, txt_two, txt_three, txt_four;

    @StringRes
    String text_two, text_four;

    @ViewById
    LinearLayout ll_born;

    @ViewById
    ImageView ad_one,ad_two,ad_three,ad_four,ad_five,ad_six,ad_seven,ad_eight,left_gif,right_top,right_down;

    @Bean
    OttoBus bus;

    @DrawableRes
    Drawable home_search_bg;

    LocationService locationService;

    public  boolean isStart=false;

    int i;

    //ScrollView 滑动监听
    @ViewById
    GradationScrollView gradationScrollView;

    private int height;

    @AnimationRes
    Animation title_anim;

    @AfterInject
    void afterInject() {
        locationService = app.locationService;
    }


    @AfterViews
    void afterView() {

        //设置滚动轮播
        banner_Vp.setOffscreenPageLimit(3);

        banner_Vp.setPageTransformer(true, new ScalePagerTransformer());
        //设置Pager之间的间距
        banner_Vp.setPageMargin(8);
        //这里要把父类的touch事件传给子类，不然边上的会滑不动
        activity_test.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return banner_Vp.dispatchTouchEvent(event);
            }
        });
        if(isNetworkAvailable(getActivity())){
            getAdvert();
        }
        //设置动画
        rl_search_two.setAnimation(title_anim);

        //设置透明
        myTitleBar.getBackground().mutate().setAlpha(0);

        myTitleBar.bringToFront();
//        AndroidTool.showLoadDialog(this);
        bus.register(this);

        locationService.registerListener(this);

        locationService.setLocationOption(locationService.getDefaultLocationClientOption());

        locationService.start();
        //设置自定义描述动画  ---- 去掉秒速灰色透明背景
//        homeSlider.setCustomAnimation(new CustomDescriptionAnimation());
        //判断首页数据是否加载出来 如果没有 就重新拉去数据
        if (app.getNewBannerList().size() >= 0) {
            setBanner();
        } else {
            AndroidTool.showLoadDialog(this);

            myBackgroundTask.getHomeBanner();

            myBackgroundTask.getAdvertByKbn();

            myBackgroundTask.getNoticeInfoList();
        }


        //滑动之前的搜索
        myTitleBar.setCustomViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity_.intent(HomeFragment.this).MouthSearch("0").start();
            }
        });
        //滑动之后的搜索
        rl_search_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity_.intent(HomeFragment.this).MouthSearch("0").start();
            }
        });
        //滑动监听
        initListeners();
    }



    //取滚动数据
    @Background
    void getAdvert(){
        BaseModelJson bmj=myRestClient.GetHomeAdvertBanner("19");
        afterGetAdvert(bmj);
    }
    @UiThread
    void afterGetAdvert(BaseModelJson<List<AdvertModel>>bmj){
        if (bmj==null){
            bmj=new BaseModelJson<>();
        }
        else if (bmj!=null&&bmj.Successful){
            adapter = new WelfareAdapter(getActivity());
            adapter.addDatas(bmj.Data);
            banner_Vp.setAdapter(adapter);

        }
    }

    //设置banner
    void setBanner() {
        //设置首页 轮播图
        for (BannerModel bannerModel : app.getNewBannerList()) {
            //显示 描述和图片sliderView
            GlideSliderView textSliderView = new GlideSliderView(getActivity());
            textSliderView.image(bannerModel.BannerImgUrl);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bannerModel", bannerModel);
            textSliderView.bundle(bundle);
            textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
            textSliderView.setOnSliderClickListener(this);
            homeSlider.addSlider(textSliderView);
        }
        //设置首页 公告信息
        if (app.getNoticeInfoModelList().size() > 0) {
            for (NoticeInfoModel noticeInfoModel : app.getNoticeInfoModelList()) {
                //自定义只显示文字描述的SliderView
                HornSliderView textSliderView = new HornSliderView(getActivity());
                Bundle bundle = new Bundle();
                bundle.putSerializable("noticeInfoModel", noticeInfoModel);
                textSliderView.bundle(bundle);
                textSliderView.setOnSliderClickListener(this);
                textSliderView.description(noticeInfoModel.NoticeInfoTitle);
                slider_horn.addSlider(textSliderView);

            }
        } else {
            ll_born.setVisibility(View.GONE);
        }
        //设置首页广告位
        for (final AdvertModel advertModel : app.getAdvertModelList()) {
            //判断为空隐藏本地gif图
            if (advertModel.AdvertImg==null&&20==advertModel.AdsenseTypeId){
                left_gif.setVisibility(View.GONE);
            }
            else {
                //初始化gif
                Glide.with(getActivity())
                        .load(R.drawable.left_egg_gif)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .skipMemoryCache(true)
                        .error(R.drawable.goods_default)
                        .placeholder(R.drawable.goods_default)
                        .into(left_gif);
            }
            if (advertModel.AdvertImg==null&&21==advertModel.AdsenseTypeId){
                right_top.setVisibility(View.GONE);
            }
            else {
                //初始化gif
                Glide.with(getActivity())
                        .load(R.drawable.blue_egg_gif)
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .skipMemoryCache(true)
                        .error(R.drawable.goods_default)
                        .placeholder(R.drawable.goods_default)
                        .into(right_top);
            }
            if (advertModel.AdvertImg==null&&22==advertModel.AdsenseTypeId){
                right_down.setVisibility(View.GONE);
            }
            else {
                //初始化gif
                Glide.with(getActivity())
                        .load(R.drawable.right_not_pause)
                        .error(R.drawable.goods_default)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.goods_default)
                        .into(right_down);
            }
            //加载广告位图片
            if (20==advertModel.AdsenseTypeId){

                Glide.with(getActivity())
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
                            ClassifyActivity_.intent(getActivity()).id(advertModel.InfoId).start();
                        }
                        else  if(2==advertModel.JumpType){
                            GoodsDetailActivity_.intent(getActivity()).goodsId(advertModel.InfoId).start();
                        }
                        else if(3==advertModel.JumpType){
                            SearchResultActivity_.intent(getActivity()).IsAppointmentPro("1").searchContent("").start();
                        }
                        else {
                            CommonWebViewActivity_.intent(getActivity()).linkUrl(advertModel.InfoId).title(advertModel.AdvertName).start();

                        }
                    }
                });
            }
            if (21==advertModel.AdsenseTypeId){
                Glide.with(getActivity())
                        .load(advertModel.AdvertImg)

                        .skipMemoryCache(true)
                        .crossFade()
                        .error(R.drawable.goods_default)
                        .placeholder(R.drawable.goods_default)
                        .into(ad_two);

                ad_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (1==advertModel.JumpType) {
                            // CategoryActivity_.intent(getActivity()).id(temp[1]).title(temp[2]).start();
                            ClassifyActivity_.intent(getActivity()).id(advertModel.InfoId).start();
                        }
                        else  if(2==advertModel.JumpType){
                            GoodsDetailActivity_.intent(getActivity()).goodsId(advertModel.InfoId).start();
                        }
                        else if(3==advertModel.JumpType){
                            SearchResultActivity_.intent(getActivity()).IsAppointmentPro("1").searchContent("").start();
                        }
                        else {
                            CommonWebViewActivity_.intent(getActivity()).linkUrl(advertModel.InfoId).title(advertModel.AdvertName).start();

                        }
                    }
                });
            }
            if (22==advertModel.AdsenseTypeId){
                Glide.with(getActivity())
                        .load(advertModel.AdvertImg)

                        .skipMemoryCache(true)
                        .crossFade()
                        .error(R.drawable.goods_default)
                        .placeholder(R.drawable.goods_default)
                        .into(ad_three);

                ad_three.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (1==advertModel.JumpType) {
                            // CategoryActivity_.intent(getActivity()).id(temp[1]).title(temp[2]).start();
                            ClassifyActivity_.intent(getActivity()).id(advertModel.InfoId).start();
                        }
                        else  if(2==advertModel.JumpType){
                            GoodsDetailActivity_.intent(getActivity()).goodsId(advertModel.InfoId).start();
                        }
                        else if(3==advertModel.JumpType){
                            SearchResultActivity_.intent(getActivity()).IsAppointmentPro("1").searchContent("").start();
                        }
                        else {
                            CommonWebViewActivity_.intent(getActivity()).linkUrl(advertModel.InfoId).title(advertModel.AdvertName).start();

                        }
                    }
                });
            }
            if (23==advertModel.AdsenseTypeId){
                Glide.with(getActivity())
                        .load(advertModel.AdvertImg)

                        .skipMemoryCache(true)
                        .crossFade()
                        .error(R.drawable.goods_default)
                        .placeholder(R.drawable.goods_default)
                        .into(ad_four);

                ad_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (1==advertModel.JumpType) {
                            // CategoryActivity_.intent(getActivity()).id(temp[1]).title(temp[2]).start();
                            ClassifyActivity_.intent(getActivity()).id(advertModel.InfoId).start();
                        }
                        else  if(2==advertModel.JumpType){
                            GoodsDetailActivity_.intent(getActivity()).goodsId(advertModel.InfoId).start();
                        }
                        else if(3==advertModel.JumpType){
                            SearchResultActivity_.intent(getActivity()).IsAppointmentPro("1").searchContent("").start();
                        }
                        else {
                            CommonWebViewActivity_.intent(getActivity()).linkUrl(advertModel.InfoId).title(advertModel.AdvertName).start();

                        }
                    }
                });
            }
            if (24==advertModel.AdsenseTypeId){
                Glide.with(getActivity())
                        .load(advertModel.AdvertImg)

                        .skipMemoryCache(true)
                        .crossFade()
                        .error(R.drawable.goods_default)
                        .placeholder(R.drawable.goods_default)
                        .into(ad_five);

                ad_five.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (1==advertModel.JumpType) {
                            // CategoryActivity_.intent(getActivity()).id(temp[1]).title(temp[2]).start();
                            ClassifyActivity_.intent(getActivity()).id(advertModel.InfoId).start();
                        }
                        else  if(2==advertModel.JumpType){
                            GoodsDetailActivity_.intent(getActivity()).goodsId(advertModel.InfoId).start();
                        }
                        else if(3==advertModel.JumpType){
                            SearchResultActivity_.intent(getActivity()).IsAppointmentPro("1").searchContent("").start();
                        }
                        else {
                            CommonWebViewActivity_.intent(getActivity()).linkUrl(advertModel.InfoId).title(advertModel.AdvertName).start();

                        }
                    }
                });
            }
            if (25==advertModel.AdsenseTypeId){
                Glide.with(getActivity())
                        .load(advertModel.AdvertImg)

                        .skipMemoryCache(true)
                        .crossFade()
                        .error(R.drawable.goods_default)
                        .placeholder(R.drawable.goods_default)
                        .into(ad_six);

                ad_six.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (1==advertModel.JumpType) {
                            // CategoryActivity_.intent(getActivity()).id(temp[1]).title(temp[2]).start();
                            ClassifyActivity_.intent(getActivity()).id(advertModel.InfoId).start();
                        }
                        else  if(2==advertModel.JumpType){
                            GoodsDetailActivity_.intent(getActivity()).goodsId(advertModel.InfoId).start();
                        }
                        else if(3==advertModel.JumpType){
                            SearchResultActivity_.intent(getActivity()).IsAppointmentPro("1").searchContent("").start();
                        }
                        else {
                            CommonWebViewActivity_.intent(getActivity()).linkUrl(advertModel.InfoId).title(advertModel.AdvertName).start();

                        }
                    }
                });
            }
            if (26==advertModel.AdsenseTypeId){
                Glide.with(getActivity())
                        .load(advertModel.AdvertImg)

                        .skipMemoryCache(true)
                        .crossFade()
                        .error(R.drawable.goods_default)
                        .placeholder(R.drawable.goods_default)
                        .into(ad_seven);

                ad_seven.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (1==advertModel.JumpType) {
                            // CategoryActivity_.intent(getActivity()).id(temp[1]).title(temp[2]).start();
                            ClassifyActivity_.intent(getActivity()).id(advertModel.InfoId).start();
                        }
                        else  if(2==advertModel.JumpType){
                            GoodsDetailActivity_.intent(getActivity()).goodsId(advertModel.InfoId).start();
                        }
                        else if(3==advertModel.JumpType){
                            SearchResultActivity_.intent(getActivity()).IsAppointmentPro("1").searchContent("").start();
                        }
                        else {
                            CommonWebViewActivity_.intent(getActivity()).linkUrl(advertModel.InfoId).title(advertModel.AdvertName).start();

                        }
                    }
                });
            }
            if (27==advertModel.AdsenseTypeId){
                Glide.with(getActivity())
                        .load(advertModel.AdvertImg)
                        .skipMemoryCache(true)
                        .crossFade()
                        .error(R.drawable.goods_default)
                        .placeholder(R.drawable.goods_default)
                        .into(ad_eight);

                ad_eight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (1==advertModel.JumpType) {
                            // CategoryActivity_.intent(getActivity()).id(temp[1]).title(temp[2]).start();
                            ClassifyActivity_.intent(getActivity()).id(advertModel.InfoId).start();
                        }
                        else  if(2==advertModel.JumpType){
                            GoodsDetailActivity_.intent(getActivity()).goodsId(advertModel.InfoId).start();
                        }
                        else if(3==advertModel.JumpType){
                            SearchResultActivity_.intent(getActivity()).IsAppointmentPro("1").searchContent("").start();
                        }
                        else {
                            CommonWebViewActivity_.intent(getActivity()).linkUrl(advertModel.InfoId).title(advertModel.AdvertName).start();

                        }
                    }
                });
            }

        }
    }

    //微仓头条
    @Click
    void ad_newimg(){
        OrganicEggActivity_.intent(getActivity()).start();
    }



    @Subscribe
    public void notifyUI(BaseModel bm) {
        AndroidTool.dismissLoadDialog();
        i++;
        if (i == 3) {
            setBanner();
        }
    }


    @Click
    void txt_one() {
        TaskOrderActivity_.intent(this).start();
    }

    @Click
    void txt_two() {
        ClassifyActivity_.intent(getActivity()).id("49").start();
    }

    @Click
    void txt_three() {
        DepotActivity_.intent(this).start();
    }

    @Click
    void txt_four() {
        //WashingActivity_.intent(this).start();
        // CommonWebViewActivity_.intent(this).title(text_four).linkUrl(Constants.ROOT_URL + "Qxxy").start();\
        //CategoryActivity_.intent(getActivity()).id("52").title(text_four).start();
        ClassifyActivity_.intent(getActivity()).start();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        if (slider.getBundle() != null && slider.getBundle().get("bannerModel") != null) {
            BannerModel bannerModel = (BannerModel) slider.getBundle().get("bannerModel");
            if (bannerModel != null) {
                //链接分类(1:商品详细，2：网页WebView)
                if (bannerModel.LinkType == 1) {
                    GoodsDetailActivity_.intent(this).goodsId(bannerModel.LinkUrl).start();
                } else if (bannerModel.LinkType == 2 && !"#".equals(bannerModel.LinkUrl)) {
                    CommonWebViewActivity_.intent(this).title(bannerModel.BannerName).linkUrl(bannerModel.LinkUrl).start();
                }
            }
        }
        if (slider.getBundle() != null && slider.getBundle().get("noticeInfoModel") != null) {
            NoticeInfoModel noticeInfoModel = (NoticeInfoModel) slider.getBundle().get("noticeInfoModel");
            if (noticeInfoModel != null) {
                //链接分类(1:商品详细，2：网页WebView ，3.商品分类)
                if (noticeInfoModel.JumpType == 1) {
                    GoodsDetailActivity_.intent(this).goodsId(noticeInfoModel.TargetAddress).start();
                } else if (noticeInfoModel.JumpType == 2 && !"#".equals(noticeInfoModel.TargetAddress)) {
                    CommonWebViewActivity_.intent(this).title(noticeInfoModel.NoticeInfoTitle).linkUrl(noticeInfoModel.TargetAddress).start();
                } else if (noticeInfoModel.JumpType == 3) {
                    CategoryActivity_.intent(getActivity()).id(noticeInfoModel.TargetAddress).title(noticeInfoModel.NoticeInfoTitle).start();
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            try {
                bus.unregister(this);
            } catch (Exception e) {
                Log.e("bus.unregister", e.getMessage());
            }
            homeSlider.stopAutoCycle();

            slider_horn.stopAutoCycle();
        } else {
            homeSlider.startAutoCycle();

            slider_horn.startAutoCycle();
            try {
                bus.register(this);
            } catch (Exception e) {
                Log.e("bus.register", e.getMessage());
            }
        }
    }

    //定位
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        Log.e("11111111111111", bdLocation.getProvince() + bdLocation.getCity() + bdLocation.getAddrStr() + bdLocation.getStreet() + bdLocation.getBuildingName() + bdLocation.getLocationDescribe());
        Log.e("11111111111111", bdLocation.getAddrStr());
        Log.e("11111111111111", bdLocation.getStreet() + "");
        Log.e("11111111111111", bdLocation.getBuildingName() + "");
        Log.e("11111111111111", bdLocation.getLocationDescribe() + "");
        Log.e("11111111111111", bdLocation.getAddress().province + bdLocation.getAddress().city + bdLocation.getAddress().address + bdLocation.getAddress().street + "");
        myTitleBar.setLeftText(bdLocation.getCity());

    }
    //滑动监听部分
    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = homeSlider.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                myTitleBar.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = homeSlider.getHeight();

                gradationScrollView.setScrollViewListener(HomeFragment.this);
            }
        });

    }


    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {

        if (y <= 0) {   //设置标题的背景颜色
            isStart=false;
            second_title_bar.setBackgroundColor(Color.argb((int) 0, 233,85,4));
            myTitleBar.setVisibility(View.VISIBLE);
            myTitleBar.getBackground().setAlpha(0);
            second_title_bar.setVisibility(View.GONE);
            title_anim.cancel();

        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            myTitleBar.setVisibility(View.GONE);
            second_title_bar.setVisibility(View.VISIBLE);
            if (!isStart){
                title_anim.start();
                isStart=true;
            }
            second_title_bar.setBackgroundColor(Color.argb((int) alpha, 233,85,4));


        } else {    //滑动到banner下面设置普通颜色
            second_title_bar.setBackgroundColor(Color.argb((int) 255, 233,85,4));

        }

    }

}
