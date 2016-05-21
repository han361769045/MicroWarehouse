package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.CartModel;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import me.himanshusoni.quantityview.QuantityView;

/**
 * Created by Leo on 2016/5/21.
 */
@EViewGroup(R.layout.fragment_cart_item)
public class CartItemView extends ItemView<CartModel> implements QuantityView.OnQuantityChangeListener {

    @ViewById
    CheckBox cb_select;

    @ViewById
    ImageView img_cart_goods_img;

    @ViewById
    TextView txt_goods_name, txt_goods_price;

    @StringRes
    String text_goods_price;

    @ViewById
    QuantityView quantityView;

    public CartItemView(Context context) {
        super(context);
    }


    @AfterViews
    void afterView() {
        quantityView.setQuantityClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        quantityView.setOnQuantityChangeListener(this);
    }

    @Override
    protected void init(Object... objects) {
        quantityView.setQuantity(_data.goodsNum);
        txt_goods_name.setText(_data.goodsName);
        txt_goods_price.setText(String.format(text_goods_price, _data.goodsPrice));
        cb_select.setChecked(_data.isChecked);

    }

    @CheckedChange
    void cb_select(boolean isChecked) {
        AndroidTool.showToast(context, isChecked + "");
        _data.isChecked = isChecked;
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }

    @Override
    public void onQuantityChanged(int newQuantity, boolean programmatically) {

    }

    @Override
    public void onLimitReached() {

    }
}
