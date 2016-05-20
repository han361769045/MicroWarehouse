package com.zczczy.leo.microwarehouse.fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.util.List;

/**
 * Created by Leo on 2016/5/20.
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    SliderLayout homeSlider;

    @ViewById
    TextView txt_one, txt_two, txt_three, txt_four;

    @ViewsById(value = {R.id.ad_one, R.id.ad_two, R.id.ad_three, R.id.ad_four, R.id.ad_five, R.id.ad_six, R.id.ad_seven, R.id.ad_eight, R.id.ad_nine})
    List<ImageView> imageViewList;



    @AfterViews
    void afterView() {

    }


    @Click(value = {R.id.ad_one, R.id.ad_two, R.id.ad_three, R.id.ad_four, R.id.ad_five, R.id.ad_six, R.id.ad_seven, R.id.ad_eight, R.id.ad_nine})
    void imageViewList(ImageView imageView) {

    }

}
