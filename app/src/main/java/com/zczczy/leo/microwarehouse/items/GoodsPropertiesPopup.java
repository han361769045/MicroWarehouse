package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.TakeOrderActivity_;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsAttribute;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.model.OrderModel;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.viewgroup.MyViewGroup;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorStateListRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.himanshusoni.quantityview.QuantityView;

/**
 * Created by Leo on 2016/5/7.
 */
@EViewGroup(R.layout.goods_properties_popup)
public class GoodsPropertiesPopup extends LinearLayout {

    @ViewById
    LinearLayout root, ll_child;

    @ViewById
    MyViewGroup myViewGroup;

    @ViewById
    QuantityView quantityView;

    @ViewById
    ImageView img_goods;

    @ViewById
    Button btn_confirm;

    @ColorStateListRes
    ColorStateList goods_properties_color;

    @ViewById
    TextView txt_store_count, txt_rmb;

    @StringRes
    String text_goods_price;

    @Bean
    MyErrorHandler myErrorHandler;

    @RestService
    MyRestClient myRestClient;

    @Pref
    MyPrefs_ pre;

    @StringRes
    String no_net;

    GoodsModel goods;

    Context context;

    PopupWindow popupWindow;

    List<TextView> textViews;

    boolean isCart;

    int selectedId;

    public GoodsPropertiesPopup(Context context) {
        super(context);
        this.context = context;
    }

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        quantityView.setQuantityClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setCart(boolean cart) {
        isCart = cart;
    }

    public void setData(PopupWindow popupWindow, GoodsModel goods) {
        this.popupWindow = popupWindow;
        this.goods = goods;
        textViews = new ArrayList<>();
        int i = 0;
        if (!StringUtils.isEmpty(goods.GoodsImgSl)) {
            Glide.with(context).load(goods.GoodsImgSl)
                    .centerCrop()
                    .crossFade()
                    .error(R.drawable.goods_default)
                    .bitmapTransform(new RoundedCornersTransformation(context, 10, 0))
                    .placeholder(R.drawable.goods_default)
                    .into(img_goods);
        }
        for (final GoodsAttribute g : goods.GoodsAttributeList) {
            final TextView textView = new TextView(context);
            textView.setText(g.GoodsAttributeName);
            textView.setBackgroundResource(R.drawable.goods_properties);
            textView.setTextColor(goods_properties_color);
            textView.setClickable(true);
            textView.setPadding(10, 10, 10, 10);
            textViews.add(textView);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (TextView tv : textViews) {
                        tv.setSelected(false);
                    }
                    textView.setSelected(true);
                    txt_store_count.setText(String.valueOf(g.GoodsAttributeStock));
                    txt_rmb.setVisibility(View.VISIBLE);
                    txt_rmb.setText(String.format(text_goods_price, g.GoodsAttributePrice));
                    selectedId = g.GoodsAttributeId;
                }
            });
            if (i == 0) {
                textView.performClick();
            }
            myViewGroup.addView(textView);
            i++;
        }
    }

    @Click
    void btn_confirm() {
        AndroidTool.showLoadDialog(context);
        if (isCart) {
            addShoppingCart();
        } else {
            popupWindow.dismiss();
            buy();
        }
    }

    @Background
    void buy() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterBuy(myRestClient.createSingleTempOrder(goods.GoodsInfoId, quantityView.getQuantity(), selectedId));
    }

    @UiThread
    void afterBuy(BaseModelJson<OrderModel> result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(context, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(context, result.Error);
        } else {
            TakeOrderActivity_.intent(context).model(result.Data).start();
        }
    }

    /**
     * 添加商品
     *
     * @param
     */
    @Background
    void addShoppingCart() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        Map<String, String> map = new HashMap<>(3);
        map.put("GoodsInfoId", goods.GoodsInfoId);
        map.put("ProductCount", String.valueOf(quantityView.getQuantity()));
        map.put("GoodsAttributeId", String.valueOf(selectedId));
        afterAddShoppingCart(myRestClient.addShoppingCart(map));
    }

    /**
     * 添加商品后更新UI
     *
     * @param bm
     */
    @UiThread
    void afterAddShoppingCart(BaseModel bm) {
        AndroidTool.dismissLoadDialog();
        if (bm == null) {
            AndroidTool.showToast(context, "商品添加失败");
        } else if (bm.Successful) {
            popupWindow.dismiss();
            AndroidTool.showToast(context, "商品添加成功");
        } else {
            AndroidTool.showToast(context, bm.Error);
        }
    }

    @Touch
    void root(View v, MotionEvent event) {
        int height = v.getHeight();
        int flHeight = ll_child.getHeight();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (y < height - flHeight && popupWindow != null) {
                popupWindow.dismiss();
            }
        }
    }

    @Click
    void img_close() {
        popupWindow.dismiss();
    }

}
