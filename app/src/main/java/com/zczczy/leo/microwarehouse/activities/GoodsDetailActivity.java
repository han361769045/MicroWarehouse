package com.zczczy.leo.microwarehouse.activities;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leo.lu.bannerauto.BannerLayout;
import com.leo.lu.bannerauto.bannertypes.DefaultBannerView;
import com.leo.lu.mytitlebar.MyTitleBar;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.fragments.GoodsDetailFragment;
import com.zczczy.leo.microwarehouse.fragments.GoodsDetailFragment_;
import com.zczczy.leo.microwarehouse.fragments.GoodsFragment;
import com.zczczy.leo.microwarehouse.fragments.GoodsFragment_;
import com.zczczy.leo.microwarehouse.items.GoodsCommentsItemView;
import com.zczczy.leo.microwarehouse.items.GoodsCommentsItemView_;
import com.zczczy.leo.microwarehouse.items.GoodsPropertiesPopup;
import com.zczczy.leo.microwarehouse.items.GoodsPropertiesPopup_;
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
import com.zczczy.leo.microwarehouse.views.BadgeView;

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
import java.util.Map;

/**
 * Created by Leo on 2016/5/23.
 */
@EActivity(R.layout.new_activity_goods_detail)
public class GoodsDetailActivity extends BaseActivity {

    @ViewById
    BannerLayout sliderLayout;

    @ViewById
    RelativeLayout parent;

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

    GoodsModel goods;

    @StringRes
    String text_goods_price, tip;

    BadgeView badgeView;

    boolean isCanBy;

    FragmentManager fragmentManager;

    GoodsDetailFragment goodsDetailFragment;

    GoodsFragment goodsRecommendFragment;

    PopupWindow popupWindow;

    GoodsPropertiesPopup goodsPropertiesPopup;

    String linkUrl;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
        fragmentManager = getSupportFragmentManager();
        badgeView = new BadgeView(this);
        badgeView.setBackground(8, Color.WHITE);
        badgeView.setTextColor(Color.RED);
        badgeView.setBadgeMargin(0, 5, 5, 0);
    }

    @AfterViews
    void afterView() {
        AndroidTool.showLoadDialog(this);
        badgeView.setTargetView(myTitleBar.getmRightButtonView());
        goodsPropertiesPopup = GoodsPropertiesPopup_.build(this);
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

    void showProperties(boolean isCart) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(goodsPropertiesPopup, ViewGroup.LayoutParams.MATCH_PARENT, parent.getHeight(), true);
            goodsPropertiesPopup.setData(popupWindow, goods);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            //设置SelectPicPopupWindow弹出窗体的背景
            popupWindow.setBackgroundDrawable(dw);
        }
        goodsPropertiesPopup.setCart(isCart);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Click
    void txt_add_cart() {
        if (checkUserIsLogin()) {
            if (goods != null && goods.IsUsing == 1 && goods.GoodsAttributeList != null && goods.GoodsAttributeList.size() > 0) {
                showProperties(true);
            } else {
                AndroidTool.showLoadDialog(this);
                addShoppingCart();
            }
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Background
    void getGoodsDetailById(String goodsInfoId) {
        afterGetGoodsDetailById(myRestClient.getGoodsInfoDetailById(goodsInfoId));
    }

    @UiThread
    void afterGetGoodsDetailById(BaseModelJson<GoodsModel> bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!bmj.Successful) {
            AndroidTool.showToast(this, bmj.Error);
        } else {
            goods = bmj.Data;
            goods_name.setText(bmj.Data.GodosName);
            goods_by.setText(bmj.Data.GoodsIsBy == 0 ? bmj.Data.Postage : "包邮");
            goods_sell_count.setText(String.valueOf(bmj.Data.GoodsXl));
            linkUrl = bmj.Data.StaticHtmlUrl;
            changeFragment(bmj.Data.StaticHtmlUrl);

            txt_price_delete.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txt_price_delete.setText(String.format(text_goods_price, bmj.Data.GoodsPrice));
            txt_bat_price.setText(bmj.Data.GoodsBatPrice);
            txt_price.setText(bmj.Data.GoodsPrice);

            goods_count.setText(String.valueOf(bmj.Data.GoodsStock));
            isCanBy = (Constants.Goods_UP == bmj.Data.GoodsStatus && bmj.Data.GoodsStock > 0);
            if (bmj.Data.GoodsImgList != null) {
                for (GoodsImgModel goodsImgModel : bmj.Data.GoodsImgList) {
                    DefaultBannerView textSliderView = new DefaultBannerView(this);
                    textSliderView.image(goodsImgModel.GoodsImgUrl);
                    sliderLayout.addBanner(textSliderView);
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


    @Background
    void getMyBuyCartCount() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterGetMyBuyCartCount(myRestClient.getMyBuyCartCount());
    }

    @UiThread
    void afterGetMyBuyCartCount(BaseModelJson<Integer> result) {
        if (result != null && result.Successful && result.Data != null) {
            badgeView.setBadgeCount(result.Data);
        }
    }


    @Click
    void txt_buy() {
        if (checkUserIsLogin()) {
            if (goods != null && goods.IsUsing == 1 && goods.GoodsAttributeList != null && goods.GoodsAttributeList.size() > 0) {
                showProperties(false);
            } else {
                AndroidTool.showLoadDialog(this);
                buy();
            }
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Background
    void buy() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterBuy(myRestClient.createSingleTempOrder(goodsId, 1, 0));
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
        Map<String, String> map = new HashMap<>(3);
        map.put("GoodsInfoId", goods.GoodsInfoId);
        map.put("ProductCount", "1");
        map.put("GoodsAttributeId", "0");
        afterAddShoppingCart(myRestClient.addShoppingCart(map));
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
            badgeView.incrementBadgeCount(1);
        }
    }

    void changeFragment(String parameter) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (goodsDetailFragment != null) {
            transaction.hide(goodsDetailFragment);
        }
        if (goodsRecommendFragment != null) {
            transaction.hide(goodsRecommendFragment);
        }
        if (rb_good_detail.isChecked()) {
            if (goodsDetailFragment == null) {
                goodsDetailFragment = GoodsDetailFragment_.builder().linkUrl(parameter).build();
                transaction.add(R.id.goods_detail_fragment, goodsDetailFragment);
            } else {
                transaction.show(goodsDetailFragment);
            }
        } else {
            if (goodsRecommendFragment == null) {
                goodsRecommendFragment = GoodsFragment_.builder().mGoodsModel(goods).build();
                transaction.add(R.id.goods_detail_fragment, goodsRecommendFragment);
            } else {
                transaction.show(goodsRecommendFragment);
            }
        }
        //transaction.commit();
        transaction.commitAllowingStateLoss();
    }


    @Click
    void ll_review() {
        GoodsCommentsActivity_.intent(this).goodsId(goodsId).start();
    }

    public void incrementBadgeCount(int count) {
        badgeView.incrementBadgeCount(count);
    }


    public void onPause() {
        super.onPause();
        sliderLayout.stopAutoCycle();
    }


    public void onResume() {
        super.onResume();
        sliderLayout.startAutoCycle();
        if (checkUserIsLogin())
            getMyBuyCartCount();
    }

    public void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }

}
