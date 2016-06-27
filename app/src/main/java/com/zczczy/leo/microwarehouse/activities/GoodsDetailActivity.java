package com.zczczy.leo.microwarehouse.activities;

import android.graphics.Paint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ScrollingView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.fragments.GoodsCommentsFragment;
import com.zczczy.leo.microwarehouse.fragments.GoodsCommentsFragment_;
import com.zczczy.leo.microwarehouse.fragments.GoodsDetailFragment;
import com.zczczy.leo.microwarehouse.fragments.GoodsDetailFragment_;
import com.zczczy.leo.microwarehouse.items.GoodsCommentsItemView;
import com.zczczy.leo.microwarehouse.items.GoodsCommentsItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsCommentsModel;
import com.zczczy.leo.microwarehouse.model.GoodsImgModel;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.OrderModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.tools.CustomDescriptionAnimation;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.HashMap;

/**
 * Created by Leo on 2016/5/23.
 */
@EActivity(R.layout.new_activity_goods_detail)
public class GoodsDetailActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener {

    @ViewById
    SliderLayout sliderLayout;

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    RadioButton rb_good_detail, rb_good_review;

    @ViewById
    TextView goods_name, goods_by, goods_sell_count, txt_bat_price, txt_price, txt_price_delete, goods_count;

    @ViewById
    LinearLayout ll_review, ll_sell_count, ll_bat_price, ll_price;

    @Bean
    MyErrorHandler myErrorHandler;

    @RestService
    MyRestClient myRestClient;

    @ViewById
    ScrollView myScrollView;

    @Extra
    String goodsId;

    @StringRes
    String text_goods_price, tip;

    boolean isCanBy;

    FragmentManager fragmentManager;

    GoodsDetailFragment goodsDetailFragment;

    GoodsCommentsFragment goodsCommentsFragment;

    String linkUrl, PlUrl;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
        fragmentManager = getSupportFragmentManager();
    }

    @AfterViews
    void afterView() {
        myTitleBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserIsLogin()) {
                    CartActivity_.intent(GoodsDetailActivity.this).start();
                } else {
                    LoginActivity_.intent(GoodsDetailActivity.this).start();
                }
            }
        });

        ll_bat_price.setVisibility(Constants.DEALER.equals(pre.userType().get()) ? View.VISIBLE : View.GONE);
        ll_price.setVisibility(Constants.DEALER.equals(pre.userType().get()) ? View.GONE : View.VISIBLE);
        getGoodsDetailById(goodsId);
    }


    @CheckedChange
    void rb_good_detail(boolean isChecked) {
        if (isChecked) {
            changeFragment(linkUrl);
        } else {
            changeFragment(goodsId);
        }
    }


    @Click
    void txt_add_cart() {
        if (checkUserIsLogin()) {
            if (isCanBy) {
                AndroidTool.showLoadDialog(this);
                addShoppingCart();
            } else {
                AndroidTool.showToast(this, tip);
            }
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_more_review() {
        rb_good_review.setChecked(true);
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
            linkUrl = bmj.Data.StaticHtmlUrl;
//            PlUrl = bmj.Data.PlUrl;
            PlUrl = goodsId;
            changeFragment(bmj.Data.StaticHtmlUrl);

            txt_price_delete.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txt_price_delete.setText(String.format(text_goods_price, bmj.Data.GoodsPrice));
            txt_bat_price.setText(bmj.Data.GoodsBatPrice);
            txt_price.setText(bmj.Data.GoodsPrice);

            goods_count.setText(String.valueOf(bmj.Data.GoodsStock));
            isCanBy = (Constants.Goods_UP == bmj.Data.GoodsStatus && bmj.Data.GoodsStock > 0);
            if (bmj.Data.GoodsImgList != null) {
                for (GoodsImgModel goodsImgModel : bmj.Data.GoodsImgList) {
                    DefaultSliderView textSliderView = new DefaultSliderView(this);
                    textSliderView.image(goodsImgModel.GoodsImgUrl);
                    textSliderView.setOnSliderClickListener(this);
                    sliderLayout.addSlider(textSliderView);
                }
            }
            if (bmj.Data.GoodsCommentsList != null) {
                for (GoodsCommentsModel goodsCommentsModel : bmj.Data.GoodsCommentsList) {
                    GoodsCommentsItemView goodsCommentsItemView = GoodsCommentsItemView_.build(this);
                    goodsCommentsItemView.init(goodsCommentsModel);
                    ll_review.addView(goodsCommentsItemView);
                    ll_review.addView(layoutInflater.inflate(R.layout.horizontal_line, ll_review, false));
                }
            }
        }
    }


    @Click
    void txt_buy() {
        if (checkUserIsLogin()) {
            if (isCanBy) {
                AndroidTool.showLoadDialog(this);
                buy();
            } else {
                AndroidTool.showToast(this, tip);
            }
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Background
    void buy() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterBuy(myRestClient.createSingleTempOrder(goodsId, 1));
    }

    @UiThread
    void afterBuy(BaseModelJson<OrderModel> result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            TakeOrderActivity_.intent(this).model(result.Data).start();
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
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            AndroidTool.showToast(this, "添加成功");
        }
    }

    void changeFragment(String parameter) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (goodsDetailFragment != null) {
            transaction.hide(goodsDetailFragment);
        }
        if (goodsCommentsFragment != null) {
            transaction.hide(goodsCommentsFragment);
        }
        if (rb_good_detail.isChecked()) {
            if (goodsDetailFragment == null) {
                goodsDetailFragment = GoodsDetailFragment_.builder().linkUrl(parameter).build();
                transaction.add(R.id.goods_detail_fragment, goodsDetailFragment);
            } else {
                transaction.show(goodsDetailFragment);
            }
        } else {
            if (goodsCommentsFragment == null) {
//                goodsCommentsFragment = GoodsDetailFragment_.builder().linkUrl(parameter).build();
                goodsCommentsFragment = GoodsCommentsFragment_.builder().goodsId(parameter).build();
                transaction.add(R.id.goods_detail_fragment, goodsCommentsFragment);
            } else {
                transaction.show(goodsCommentsFragment);
            }
        }
        //transaction.commit();
        transaction.commitAllowingStateLoss();
    }


    @Click
    void ll_review() {
        GoodsCommentsActivity_.intent(this).goodsId(goodsId).start();
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
