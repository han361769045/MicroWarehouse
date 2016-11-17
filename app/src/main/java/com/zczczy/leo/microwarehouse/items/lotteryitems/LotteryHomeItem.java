package com.zczczy.leo.microwarehouse.items.lotteryitems;

import android.content.Context;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;

import com.zczczy.leo.microwarehouse.items.ItemView;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.viewgroup.MyProgressBar;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

/**
 * Created by zczczy on 2016/11/10.
 */
@EViewGroup(R.layout.item_lottery_home)
public class LotteryHomeItem extends ItemView<GoodsModel> {
    @ViewById
    ImageView pic;

    @ViewById
    Button  img_add_cart;

    @StringRes
    String no_net;

    @ViewById
    TextView goods_name,total_people,more_people;

    @ViewById
    MyProgressBar pb_progressbar;

    @ViewById
    LinearLayout ll_bat_price, ll_price, ll_delete_price,ll_activity_now,ll_complete;

    @Pref
    MyPrefs_ pre;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    boolean isCanBy;

    public LotteryHomeItem(Context context) {
        super(context);
    }

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @Override
    protected void init(Object... objects) {
        ll_activity_now.setVisibility(GONE);
        ll_complete.setVisibility(VISIBLE);
        pb_progressbar.setProgress(80);
        if (!StringUtils.isEmpty(_data.GoodsImgSl)) {
            Glide.with(context).load(_data.GoodsImgSl)
                    .skipMemoryCache(true)
                    .crossFade().
                    centerCrop().placeholder(R.drawable.goods_default).error(R.drawable.goods_default).into(pic);
        }

        goods_name.setText(_data.GodosName);
        total_people.setText(_data.GoodsPrice);
        more_people.setText(_data.GoodsPrice);
        isCanBy = (Constants.Goods_UP == _data.GoodsStatus && _data.GoodsStock > 0);
    }
    @Click
    void img_add_cart() {
//        if (StringUtils.isEmpty(pre.token().get())) {
//            AndroidTool.showToast(context, "请登录");
//        } else {
//            if (isCanBy) {
//                AndroidTool.showLoadDialog(context);
//                addShoppingCart();
//            } else {
//                AndroidTool.showToast(context, tip);
//            }
//        }
        GoodsDetailActivity_.intent(context).goodsId(_data.GoodsInfoId).start();
    }

    @Background
    void addShoppingCart() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
//        afterAddShoppingCart(myRestClient.addShoppingCart(_data.GoodsInfoId));
    }

    @UiThread
    void afterAddShoppingCart(BaseModel result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(context, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(context, result.Error);
        } else {
            AndroidTool.showToast(context, "添加成功");
        }
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }

}
