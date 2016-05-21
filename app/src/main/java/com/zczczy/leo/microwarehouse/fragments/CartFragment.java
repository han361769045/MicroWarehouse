package com.zczczy.leo.microwarehouse.fragments;

import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.CartAdapter;
import com.zczczy.leo.microwarehouse.model.CartModel;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

/**
 * Created by Leo on 2016/5/20.
 */
@EFragment(R.layout.fragment_cart)
public class CartFragment extends BaseRecyclerViewFragment<CartModel> {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    LinearLayout ll_checkout, ll_delete;

    @ViewById
    CheckBox cb_all;

    @ViewById
    TextView txt_total_lb;

    @StringRes
    String cart_total;

    @Bean
    void setAdapter(CartAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @CheckedChange
    void cb_all(boolean isChecked) {
        for (CartModel cartModel : myAdapter.getItems()) {
            cartModel.isChecked = isChecked;
        }
        myAdapter.notifyItemRangeChanged(0, myAdapter.getItemCount());
    }

}
