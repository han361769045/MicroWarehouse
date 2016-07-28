package com.zczczy.leo.microwarehouse.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.FlexibleDividerDecoration;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.CartAdapter;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.CartModel;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2016/5/26.
 */
@EActivity(R.layout.fragment_cart)
public class CartActivity extends BaseRecyclerViewActivity<CartModel> {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    LinearLayout ll_checkout, ll_delete, ll_nothing;

    @ViewById
    CheckBox cb_all;

    @ViewById
    TextView txt_total_lb, txt_checkout, txt_delete;

    @ViewById
    ImageView img_soon;

    @StringRes
    String cart_total, text_buy, text_edit, text_delete, text_cancel, text_tip, text_tip_confirm;

    @Bean
    OttoBus bus;

    //总钱数
    int count = 0;

    // 购物车 ids
    String ids;

    //
    List<CartModel> list;

    @AfterViews
    void afterView() {
        bus.register(this);
        myTitleBar.hideLogo();
        recyclerView.removeItemDecoration(itemDecoration);
        itemDecoration = new HorizontalDividerItemDecoration.Builder(this).margin(21)
                .visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
                    @Override
                    public boolean shouldHideDivider(int position, RecyclerView parent) {
                        return position == parent.getAdapter().getItemCount() - 2;
                    }
                }).paint(paint).build();
        recyclerView.addItemDecoration(itemDecoration);
        txt_total_lb.setText(String.format(cart_total, 0.0));
        txt_checkout.setText(String.format(text_buy, count));
        txt_delete.setText(String.format(text_delete, count));
        list = new ArrayList<>();
        myTitleBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_delete.isShown()) {
                    ll_delete.setVisibility(View.GONE);
                    myTitleBar.setRightText(text_edit);
                    ll_checkout.setVisibility(View.VISIBLE);

                } else {
                    myTitleBar.setRightText(text_cancel);
                    ll_delete.setVisibility(View.VISIBLE);
                    ll_checkout.setVisibility(View.GONE);
                }
            }
        });
        img_soon.setVisibility(View.GONE);
    }

    @Bean
    void setAdapter(CartAdapter myAdapter) {
        this.myAdapter = myAdapter;
        myAdapter.isFooter = true;
    }

    @Click
    void img_soon() {

    }


    @Override
    public void onResume() {
        super.onResume();
        myAdapter.getMoreData();
    }

    @Click
    void ll_delete() {
        calcMoney();
        if (count > 0) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle(text_tip).setMessage(text_tip_confirm).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AndroidTool.showLoadDialog(CartActivity.this);
                    deleteShopping();
                }
            }).setNegativeButton(text_cancel, null).setIcon(R.mipmap.ic_launcher).create().show();
        } else {
            AndroidTool.showToast(this, "您还没有选中任何商品哟~");
        }
    }

    @Background
    void deleteShopping() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterDeleteShopping(myRestClient.deleteShoppingCartById(ids));
    }

    @UiThread
    void afterDeleteShopping(BaseModel bm) {
        AndroidTool.dismissLoadDialog();
        if (bm == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!bm.Successful) {
            AndroidTool.showToast(this, bm.Error);
        } else {
            myAdapter.deleteItemRange(list);
            ll_delete.setVisibility(View.GONE);
            myTitleBar.setRightText(text_edit);
            ll_checkout.setVisibility(View.VISIBLE);
            calcMoney();
        }
    }

    @Click
    void ll_checkout() {
        calcMoney();
        if (ids.length() > 0) {
            TakeOrderActivity_.intent(this).ids(ids).start();
        } else {
            AndroidTool.showToast(this, "您还没有选中任何商品哟~");
        }
    }


    @Click
    void cb_all() {
        for (CartModel cartModel : myAdapter.getItems()) {
            cartModel.isChecked = cb_all.isChecked();
        }
        //通知Adapter选择状态
        myAdapter.notifyItemRangeChanged(0, myAdapter.getAdapterItemCount());
        //计算
        calcMoney();
    }

    @Subscribe
    public void notifyUI(Object isChecked) {
        //判断是否全部选中  是 则全选选中 否者不选中
        for (CartModel cartModel : myAdapter.getItems()) {
            if (!cartModel.isChecked) {
                cb_all.setChecked(false);
                break;
            } else {
                cb_all.setChecked(true);
            }
        }
        calcMoney();
    }

    //计算当前所选的金额和数量
    void calcMoney() {
        if (myAdapter.getAdapterItemCount() < 1) {
            ll_nothing.setVisibility(View.VISIBLE);
        } else {
            ll_nothing.setVisibility(View.GONE);
        }
        double totalMoney = 0.00;
        count = 0;
        ids = "";
        list.clear();
        for (CartModel cartModel : myAdapter.getItems()) {
            if (cartModel.isChecked) {
                totalMoney += cartModel.ProductCount * cartModel.GoodsPrice;
                count += cartModel.ProductCount;
                ids += cartModel.BuyCartInfoId + ",";
                list.add(cartModel);
            }
        }
        if (ids.length() > 0) {
            ids = ids.substring(0, ids.lastIndexOf(','));
        }
        txt_total_lb.setText(String.format(cart_total, AndroidTool.get2Double(totalMoney)));
        txt_checkout.setText(String.format(text_buy, count));
        txt_delete.setText(String.format(text_delete, count));
    }

    public void finish() {
        super.finish();
        bus.unregister(this);
    }
}
