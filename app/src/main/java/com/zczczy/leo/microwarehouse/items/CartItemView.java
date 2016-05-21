package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.model.CartModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

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
