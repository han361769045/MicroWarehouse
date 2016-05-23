package com.zczczy.leo.microwarehouse.activities;

import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.viewgroup.MyScrollView;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by Leo on 2016/5/23.
 */
@EActivity(R.layout.activity_goods_detail)
public class GoodsDetailActivity extends BaseActivity implements MyScrollView.OnScrollListener, BaseSliderView.OnSliderClickListener {

    @ViewById
    SliderLayout sliderLayout;

    @ViewById
    MyScrollView myScrollView;

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    RelativeLayout theViewStay;

    @ViewById
    View mBuyLayout;

    @ViewById
    TextView txt_address;

    @ViewById
    LinearLayout parent, ll_review, ll_sell_count, ll_goods_by;

    @ViewById
    WebView web_view;

    @Bean
    MyErrorHandler myErrorHandler;

    @RestService
    MyRestClient myRestClient;

    @Extra
    String goodsId;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }


    @AfterViews
    void afterView() {
        myScrollView.setOnScrollListener(this);
        // 当布局的状态或者控件的可见性发生改变回调的接口
        parent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // 这一步很重要，使得上面的购买布局和下面的购买布局重合
                        onScroll(myScrollView.getScrollY());
                    }
                });
        getGoodsDetailById(goodsId);
//        getGoodsComments(goodsId);

    }

    @Background
    void getGoodsDetailById(String goodsInfoId) {
//        afterGetGoodsDetailById(myRestClient.getGoodsDetailById(goodsInfoId));
    }

    @UiThread
    void afterGetGoodsDetailById(BaseModelJson<GoodsModel> bmj) {

    }

    /**
     * 控制购买浮动条的 位置 上面的购买布局（theViewStay）和下面的购买布局（mBuyLayout）重合起来了
     * layout()这个方法是确定View的大小和位置的
     * ，然后将其绘制出来，里面的四个参数分别是View的四个点的坐标，他的坐标不是相对屏幕的原点，而且相对于他的父布局来说的，
     */
    @Override
    public void onScroll(int scrollY) {
        int mBuyLayout2ParentTop = Math.max(scrollY, mBuyLayout.getTop());
        theViewStay.layout(0, mBuyLayout2ParentTop, theViewStay.getWidth(), mBuyLayout2ParentTop + theViewStay.getHeight());
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
