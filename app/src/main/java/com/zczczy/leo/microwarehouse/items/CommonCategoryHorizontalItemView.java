package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

/**
 * Created by Leo on 2016/5/21.
 */
@EViewGroup(R.layout.category_goods_item_horizontal)
public class CommonCategoryHorizontalItemView extends ItemView<GoodsModel> {

    @ViewById
    ImageView pic, img_add_cart;

    @ViewById
    TextView goods_name, goods_sell_count, goods_price, goods_bat_price;

    @StringRes
    String no_net, text_goods_sell_count, text_goods_price, tip;

    @Pref
    MyPrefs_ pre;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;


    boolean isCanBy;

    public CommonCategoryHorizontalItemView(Context context) {
        super(context);
    }

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @Override
    protected void init(Object... objects) {
        if (!StringUtils.isEmpty(_data.GoodsImgSl)) {
            Picasso.with(context).load(_data.GoodsImgSl).resize(200, 200).
                    centerCrop().placeholder(R.drawable.goods_default).error(R.drawable.goods_default).into(pic);
        }

        goods_name.setText(_data.GodosName);
        goods_sell_count.setText(String.format(text_goods_sell_count, _data.GoodsXl));
        goods_price.setText(String.format(text_goods_price, _data.GoodsPrice));
        goods_bat_price.setText(String.format(text_goods_price, _data.GoodsBatPrice));
        isCanBy = (Constants.Goods_UP == _data.GoodsStatus && _data.GoodsStock > 0);
    }


    @Click
    void img_add_cart() {
        if (StringUtils.isEmpty(pre.token().get())) {
            AndroidTool.showToast(context, "请登录");
        } else {
            if (isCanBy) {
                AndroidTool.showLoadDialog(context);
                addShoppingCart();
            } else {
                AndroidTool.showToast(context, tip);
            }
        }
    }

    @Background
    void addShoppingCart() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterAddShoppingCart(myRestClient.addShoppingCart(_data.GoodsInfoId));
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
