package com.zczczy.leo.microwarehouse.fragments;

import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.Banner;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;
import org.androidannotations.rest.spring.annotations.RestService;

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

    @ViewById
    TextView txt_one, txt_two, txt_three, txt_four;

    @ViewsById(value = {R.id.ad_one, R.id.ad_two, R.id.ad_three, R.id.ad_four, R.id.ad_five, R.id.ad_six, R.id.ad_seven, R.id.ad_eight, R.id.ad_nine})
    List<ImageView> imageViewList;



    @RestService
    MyRestClient myRestClient;
    @Bean
    MyErrorHandler myErrorHandler;

    @AfterViews
    void afterView() {

    }


    @Click(value = {R.id.ad_one, R.id.ad_two, R.id.ad_three, R.id.ad_four, R.id.ad_five, R.id.ad_six, R.id.ad_seven, R.id.ad_eight, R.id.ad_nine})
    void imageViewList(ImageView imageView) {

    }
//DE99B9413576BE23

//    //查询banner
//    @Background
//    void GetBanner(){
//              myRestClient.setRestErrorHandler(myErrorHandler);
//        BaseModelJson<List<Banner>>bmj=myRestClient.getHomeBanner();
//        afterGetBanner(bmj);
//    }
//    @UiThread
//    void afterGetBanner(BaseModelJson<List<Banner>>bmj){
//        if (bmj != null) {
//            if (bmj.Successful) {
//                for (int i = 0; i < bmj.Data.size(); i++) {
//                    TextSliderView textSliderView = new TextSliderView(getActivity());
//                    textSliderView.image(bmj.Data.get(i).BannerImgUrl);
//                    textSliderView.setOnSliderClickListener(this);
//                    homeSlider.addSlider(textSliderView);
//                }
//            }
//        }
//    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        homeSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        homeSlider.startAutoCycle();
    }
}
