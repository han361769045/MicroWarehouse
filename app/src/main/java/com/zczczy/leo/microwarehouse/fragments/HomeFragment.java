package com.zczczy.leo.microwarehouse.fragments;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.CategoryActivity_;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;
import com.zczczy.leo.microwarehouse.activities.SearchActivity_;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.AdvertModel;
import com.zczczy.leo.microwarehouse.model.BannerModel;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.tools.DensityUtil;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;
import org.androidannotations.annotations.res.DrawableRes;
import org.androidannotations.api.UiThreadExecutor;

import java.util.List;

/**
 * Created by Leo on 2016/5/20.
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    SliderLayout homeSlider;

    @Bean
    MyBackgroundTask myBackgroundTask;

    @ViewById
    TextView txt_one, txt_two, txt_three, txt_four, txt_horn;

    @ViewById
    LinearLayout ll_born;

    @ViewsById(value = {R.id.ad_one, R.id.ad_two, R.id.ad_three, R.id.ad_four,
            R.id.ad_five, R.id.ad_six, R.id.ad_seven, R.id.ad_eight, R.id.ad_nine,
            R.id.ad_ten, R.id.ad_eleven
    })
    List<ImageView> imageViewList;

    @Bean
    OttoBus bus;

    @DrawableRes
    Drawable home_search_bg;

    int i;

    int noticeIndex;

    @AfterViews
    void afterView() {
        bus.register(this);
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
    }

    void setBanner() {
        for (BannerModel bannerModel : app.getNewBannerList()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView.image(bannerModel.BannerImgUrl);
            textSliderView.setOnSliderClickListener(this);
            homeSlider.addSlider(textSliderView);
        }
        int i = 0;
        for (AdvertModel advertModel : app.getAdvertModelList()) {
            RequestCreator rc = Picasso.with(getActivity()).load(advertModel.AdvertImg);
            rc.into(imageViewList.get(i));
            imageViewList.get(i).setContentDescription(advertModel.JumpType + "," + advertModel.InfoId);
            i++;
            if (i == 9) {
                break;
            }
        }

        if (app.getNoticeInfoModelList().size() > 0) {
            setNotice();
        } else {
            ll_born.setVisibility(View.GONE);
        }
    }

    @UiThread(delay = 3000, id = "notice")
    void setNotice() {
        if (noticeIndex >= app.getNoticeInfoModelList().size()) {
            noticeIndex = noticeIndex % app.getNoticeInfoModelList().size();
        }
        txt_horn.setText(app.getNoticeInfoModelList().get(noticeIndex).NoticeInfoTitle);
        noticeIndex++;
        setNotice();
    }


    @Subscribe
    public void notifyUI(BaseModel bm) {
        AndroidTool.dismissLoadDialog();
        i++;
        if (i == 3) {
            setBanner();
        }
    }

    @Click(value = {R.id.ad_one, R.id.ad_two, R.id.ad_three, R.id.ad_four, R.id.ad_five, R.id.ad_six, R.id.ad_seven, R.id.ad_eight, R.id.ad_nine, R.id.ad_ten, R.id.ad_eleven})
    void imageViewList(ImageView imageView) {
        if (imageView.getContentDescription() != null) {
            String[] temp = imageView.getContentDescription().toString().split(",");
            if (temp.length == 2) {
                //跳转标识(1:商品类别页，2：商品明细)
                if (Constants.GOODS_TYPE.equals(temp[0])) {
                    CategoryActivity_.intent(getActivity()).id(temp[1]).start();
                } else {
                    GoodsDetailActivity_.intent(getActivity()).goodsId(temp[1]).start();
                }
            }
        }
    }

    @Click
    void txt_one() {
//        SearchResultActivity_.intent(this).start();
    }

    @Click
    void txt_two() {
//        SearchActivity_.intent(this).start();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            bus.unregister(this);
            homeSlider.stopAutoCycle();
            UiThreadExecutor.cancelAll("notice");
        } else {
            homeSlider.startAutoCycle();
            bus.register(this);
            setNotice();
        }
    }

}
