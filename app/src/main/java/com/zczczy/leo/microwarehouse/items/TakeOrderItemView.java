package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.springframework.util.StringUtils;

/**
 * Created by Leo on 2016/5/5.
 */
@EViewGroup(R.layout.take_order_item)
public class TakeOrderItemView extends ItemView<OrderDetailModel> {

    @ViewById
    ImageView img_cart_goods_img;

    @ViewById
    TextView txt_cart_goods_product, txt_goods_price, txt_goods_bat_price, txt_num;

    @StringRes
    String text_goods_price;

    public TakeOrderItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        if (!StringUtils.isEmpty(_data.GoodsImgSl)) {
            Glide.with(context)
                    .load(_data.GoodsImgSl)
                    .skipMemoryCache(true)
                    .crossFade()
                    .centerCrop()
                    .placeholder(R.drawable.goods_default)
                    .error(R.drawable.goods_default)
                    .into(img_cart_goods_img);
        }
        txt_cart_goods_product.setText(_data.ProductName);
        txt_goods_price.setText(String.format(text_goods_price, _data.ProductPrice));
        txt_num.setText("x".concat(String.valueOf(_data.ProductNum)));
    }


    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
