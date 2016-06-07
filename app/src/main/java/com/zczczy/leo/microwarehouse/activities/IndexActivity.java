package com.zczczy.leo.microwarehouse.activities;

import android.util.Log;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.zczczy.leo.microwarehouse.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/20.
 */
@EActivity(R.layout.activity_index)
public class IndexActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener {

    @ViewById
    SliderLayout sliderLayout;

    @ViewById
    PagerIndicator custom_indicator;

    @AfterViews
    void afterView() {
        if (pre.isFirst().get()) {
            showIndex();
        } else {
            WelcomeActivity_.intent(this).start();
            finish();
        }
    }

    void showIndex() {
        sliderLayout.stopAutoCycle();

        DefaultSliderView textSliderView = new DefaultSliderView(this);
        textSliderView.image(R.drawable.goods_default);
        textSliderView.setOnSliderClickListener(this);
        sliderLayout.addSlider(textSliderView);

        DefaultSliderView textSliderView1 = new DefaultSliderView(this);
        textSliderView1.image(R.drawable.goods_default);
        textSliderView1.setOnSliderClickListener(this);
        sliderLayout.addSlider(textSliderView1);

        DefaultSliderView textSliderView2 = new DefaultSliderView(this);
        textSliderView2.image(R.drawable.goods_default);
        textSliderView2.setOnSliderClickListener(this);
        sliderLayout.addSlider(textSliderView2);

//        sliderLayout.setCustomIndicator(custom_indicator);

        sliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("onPageScrolled", "position=" + position + ",positionOffset=" + positionOffset + ",positionOffsetPixels=" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("onPageSelected", "position=" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("onPageScrollStateC", "state=" + state);
            }
        });
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        Log.e("111111111", "sliderLayout=" + sliderLayout.getCurrentPosition() + "------" + (sliderLayout.getChildCount() - 1));
        if (sliderLayout.getCurrentPosition() == 2) {
            WelcomeActivity_.intent(this).start();
            pre.isFirst().put(false);
            finish();
        }
    }
}
