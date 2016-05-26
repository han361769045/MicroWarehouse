package com.zczczy.leo.microwarehouse.activities;

import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.items.GoodsCommentsItemView;
import com.zczczy.leo.microwarehouse.items.GoodsCommentsItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsCommentsModel;
import com.zczczy.leo.microwarehouse.model.GoodsImgModel;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.viewgroup.MyScrollView;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
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
    TextView goods_name, goods_by, goods_sell_count, txt_rmb, txt_home_lb, goods_count;

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

    @StringRes
    String text_goods_price, text_bat_price, text_price, tip;

    WebSettings settings;

    boolean isCanBy;

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

        myTitleBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserIsLogin()) {
                    CartActivity_.intent(GoodsDetailActivity.this).start();
                } else {
                    AndroidTool.showToast(GoodsDetailActivity.this, "请登录");
                }
            }
        });


        settings = web_view.getSettings();
        settings.setJavaScriptEnabled(true);
        web_view.getSettings().setAllowFileAccess(true);
        web_view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存
        getGoodsDetailById(goodsId);
    }

    @Click
    void card_buy() {
        if (checkUserIsLogin()) {
            if (isCanBy) {
                addShoppingCart();
            } else {
                AndroidTool.showToast(this, tip);
            }

        } else {
            AndroidTool.showToast(this, "请登录");
        }
    }

    @Background
    void getGoodsDetailById(String goodsInfoId) {
        afterGetGoodsDetailById(myRestClient.getGoodsInfoDetailById(goodsInfoId));
    }

    @UiThread
    void afterGetGoodsDetailById(BaseModelJson<GoodsModel> bmj) {
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!bmj.Successful) {
            AndroidTool.showToast(this, bmj.Error);
        } else {
            goods_name.setText(bmj.Data.GodosName);
            goods_by.setText(bmj.Data.GoodsIsBy == 0 ? bmj.Data.Postage : "包邮");
            goods_sell_count.setText(String.valueOf(bmj.Data.GoodsXl));
            web_view.loadUrl(bmj.Data.StaticHtmlUrl);
            txt_rmb.setText(String.format(text_price + text_goods_price, bmj.Data.GoodsPrice));
            txt_home_lb.setText(String.format(text_bat_price + text_goods_price, bmj.Data.GoodsBatPrice));
            goods_count.setText(String.valueOf(bmj.Data.GoodsStock));
            isCanBy = (Constants.Goods_UP == bmj.Data.GoodsStatus && bmj.Data.GoodsStock > 0);
            if (bmj.Data.GoodsImgList != null) {
                for (GoodsImgModel goodsImgModel : bmj.Data.GoodsImgList) {
                    TextSliderView textSliderView = new TextSliderView(this);
                    textSliderView.image(goodsImgModel.GoodsImgUrl);
                    textSliderView.setOnSliderClickListener(this);
                    sliderLayout.addSlider(textSliderView);
                }
            }
            if (bmj.Data.GoodsComments != null) {
                GoodsCommentsItemView goodsCommentsItemView = GoodsCommentsItemView_.build(this);
                goodsCommentsItemView.init(bmj.Data.GoodsComments);
                ll_review.addView(goodsCommentsItemView);
                ll_review.addView(layoutInflater.inflate(R.layout.horizontal_line, null));
            }
        }
    }


    @Background
    void addShoppingCart() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterAddShoppingCart(myRestClient.addShoppingCart(goodsId));
    }

    @UiThread
    void afterAddShoppingCart(BaseModel result) {
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            AndroidTool.showToast(this, "添加成功");
        }
    }


    @Click
    void ll_review() {
        GoodsCommentsActivity_.intent(this).goodsId(goodsId).start();
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


    public void onPause() {
        super.onPause();
        sliderLayout.stopAutoCycle();
    }


    public void onResume() {
        super.onResume();
        sliderLayout.startAutoCycle();
    }
}
