package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.GoodsModel;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

/**
 * Created by Leo on 2016/5/21.
 */
@EViewGroup(R.layout.goods_item_vertical)
public class GoodsVerticalItemView extends ItemView<GoodsModel> {

    @ViewById
    ImageView pic, img_add_cart;

    @ViewById
    TextView goods_name, goods_sell_count, goods_price;

    @StringRes
    String text_goods_sell_count, text_goods_price;

    public GoodsVerticalItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        goods_name.setText(_data.goodsName);
        goods_sell_count.setText(String.format(text_goods_sell_count, 100));
        goods_price.setText(String.format(text_goods_price, _data.goodsPrice));
    }


    @Click
    void img_add_cart() {

    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
