package com.zczczy.leo.microwarehouse.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.leo.lu.mytitlebar.MyTitleBar;
import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.CategoryActivity_;
import com.zczczy.leo.microwarehouse.activities.CommonWebViewActivity_;
import com.zczczy.leo.microwarehouse.activities.DepotActivity_;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;
import com.zczczy.leo.microwarehouse.activities.SearchActivity_;
import com.zczczy.leo.microwarehouse.activities.TaskOrderActivity_;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.model.BannerModel;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.NoticeInfoModel;
import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;
import com.zczczy.leo.microwarehouse.service.LocationService;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.viewgroup.HornSliderView;
import com.zczczy.leo.microwarehouse.views.GlideSliderView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;
import org.androidannotations.annotations.res.DrawableRes;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;

/**
 * Created by Leo on 2016/5/20.
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener, BDLocationListener {

    @ViewById
    MyTitleBar myTitleBar, my_title_bar_s;

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

    @ViewsById(value = {R.id.ad_one, R.id.ad_two, R.id.ad_three, R.id.ad_four,
            R.id.ad_five, R.id.ad_six, R.id.ad_seven, R.id.ad_eight, R.id.ad_nine,
            R.id.ad_ten, R.id.ad_eleven, R.id.ad_twelve, R.id.ad_thirteen, R.id.ad_fourteenth,
            R.id.ad_fifteen, R.id.ad_sixteen, R.id.ad_seventeen
    })
    List<ImageView> imageViewList;

    @Bean
    OttoBus bus;

    @DrawableRes
    Drawable home_search_bg;

    LocationService locationService;

    int i;

    @AfterInject
    void afterInject() {
        locationService = app.locationService;
    }

    @AfterViews
    void afterView() {
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

        myTitleBar.setCustomViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity_.intent(HomeFragment.this).start();
            }
        });

        my_title_bar_s.setCustomViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity_.intent(HomeFragment.this).start();
            }
        });


        myTitleBar.setLeftTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

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
        int i = 0;
        for (AdvertModel advertModel : app.getAdvertModelList()) {
            Glide.with(getActivity())
                    .load(advertModel.AdvertImg)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .crossFade()
                    .error(R.drawable.goods_default)
                    .placeholder(R.drawable.goods_default)
                    .into(imageViewList.get(i));
            //为每一个广告位添加描述（传参数）
            imageViewList.get(i).setContentDescription(advertModel.JumpType + "," + advertModel.InfoId + "," + advertModel.AdvertName);
            i++;
            if (i == 17) {
                break;
            }
        }
    }


    @Subscribe
    public void notifyUI(BaseModel bm) {
        AndroidTool.dismissLoadDialog();
        i++;
        if (i == 3) {
            setBanner();
        }
    }

    @Click(value = {R.id.ad_one, R.id.ad_two, R.id.ad_three, R.id.ad_four, R.id.ad_five, R.id.ad_six, R.id.ad_seven, R.id.ad_eight, R.id.ad_nine, R.id.ad_ten, R.id.ad_eleven, R.id.ad_twelve, R.id.ad_thirteen, R.id.ad_fourteenth, R.id.ad_fifteen, R.id.ad_sixteen, R.id.ad_seventeen})
    void imageViewList(ImageView imageView) {
        if (imageView.getContentDescription() != null) {
            String[] temp = imageView.getContentDescription().toString().split(",");
            if (temp.length == 3) {
                //跳转标识(1:商品类别页，2：商品明细)
                if (Constants.GOODS_TYPE.equals(temp[0])) {
                    CategoryActivity_.intent(getActivity()).id(temp[1]).title(temp[2]).start();
                } else {
                    GoodsDetailActivity_.intent(getActivity()).goodsId(temp[1]).start();
                }
            }
        }
    }

    @Click
    void txt_one() {
        TaskOrderActivity_.intent(this).start();
    }

    @Click
    void txt_two() {
        CategoryActivity_.intent(getActivity()).id("49").title(text_two).start();
    }

    @Click
    void txt_three() {
        DepotActivity_.intent(this).start();
    }

    @Click
    void txt_four() {
//        WashingActivity_.intent(this).start();
        CommonWebViewActivity_.intent(this).title(text_four).linkUrl(Constants.ROOT_URL + "Qxxy").start();
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

}
