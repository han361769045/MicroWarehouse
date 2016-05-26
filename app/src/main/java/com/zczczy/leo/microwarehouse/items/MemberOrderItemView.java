package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.LogisticsInfoActivity_;
import com.zczczy.leo.microwarehouse.activities.MemberOrderActivity;
import com.zczczy.leo.microwarehouse.activities.OrderDetailActivity_;
import com.zczczy.leo.microwarehouse.activities.UmspayActivity_;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;
import com.zczczy.leo.microwarehouse.model.OrderModel;
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
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leo on 2016/5/6.
 */
@EViewGroup(R.layout.activity_member_order_item)
public class MemberOrderItemView extends ItemView<OrderModel> {


    @ViewById
    TextView txt_count, txt_rmb, txt_do_message;

    @ViewById
    LinearLayout ll_pre_order_item, ll_take;

    @ViewById
    Button btn_canceled, btn_cancel_order, btn_logistics, btn_pay, btn_finish, btn_finished;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @Pref
    MyPrefs_ pre;

    @StringRes
    String no_net, text_count, text_goods_price;

    MemberOrderActivity memberOrderActivity;

    Context context;

    public MemberOrderItemView(Context context) {
        super(context);
        this.context = context;
        memberOrderActivity = (MemberOrderActivity) context;
    }


    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }


    @Click
    void btn_canceled() {
        OrderDetailActivity_.intent(context).orderId(_data.MOrderId).start();
    }

    @Click
    void btn_finish() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle("提示").setMessage("确认收货？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AndroidTool.showLoadDialog(context);
                confirmReceipt();
            }
        }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
    }

    @Background
    void confirmReceipt() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterConfirm(myRestClient.confirmSh(_data.MOrderId));
    }

    @UiThread
    void afterConfirm(BaseModel bm) {
        AndroidTool.dismissLoadDialog();
        if (bm == null) {
            AndroidTool.showToast(context, no_net);
        } else if (!bm.Successful) {
            AndroidTool.showToast(context, bm.Error);
        } else {
            btn_finish.setVisibility(GONE);
            btn_finished.setVisibility(VISIBLE);
            baseUltimateRecyclerViewAdapter.getItems().remove(_data);
            baseUltimateRecyclerViewAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    }


    @Click
    void btn_cancel_order() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle("提示").setMessage("确认取消订单？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AndroidTool.showLoadDialog(context);
                cancelOrder();
            }
        }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
    }


    @Background
    void cancelOrder() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterCancelOrder(myRestClient.cancelOrderByOrderId(_data.MOrderId));
    }

    @UiThread
    void afterCancelOrder(BaseModel result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(context, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(context, result.Error);

        } else {
            baseUltimateRecyclerViewAdapter.getItems().remove(_data);
            baseUltimateRecyclerViewAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    }


    @Click
    void btn_logistics() {
        LogisticsInfoActivity_.intent(context).MOrderId(_data.MOrderId).start();
    }

    @Click
    void btn_pay() {
        UmspayActivity_.intent(memberOrderActivity).order(_data).startForResult(1000);
    }

    @Click
    void btn_finished() {

    }


    @Override
    protected void init(Object... objects) {
//        //设置商品总数
        txt_count.setText(String.format(text_count, _data.GoodsAllCount));
        //设置商品总价
        txt_rmb.setText(String.format(text_goods_price, _data.MOrderMoney));
        //先清空所以布局
        ll_pre_order_item.removeAllViews();
        for (OrderDetailModel orderDetailModel : _data.MOrderDetailList) {
            TakeOrderItemView preOrderItemView = TakeOrderItemView_.build(context);
            preOrderItemView.init(orderDetailModel);
            ll_pre_order_item.addView(preOrderItemView);
        }
        txt_do_message.setVisibility(VISIBLE);
        txt_do_message.setText(_data.MorderStatusDisp);
        if (_data.MorderStatus == Constants.DUEPAYMENT) {
            ll_take.setVisibility(VISIBLE);
            btn_cancel_order.setVisibility(VISIBLE);
            btn_pay.setVisibility(_data.MPaymentType == 1 ? VISIBLE : GONE);
            btn_canceled.setVisibility(GONE);
            btn_logistics.setVisibility(GONE);
            btn_finish.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.PAID) {
            ll_take.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.SELECT_) {
            ll_take.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.RECEIVER) {
            ll_take.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.SHIPPING) {
            ll_take.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_finish.setVisibility(VISIBLE);
            btn_cancel_order.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.CONFIRM) {
            ll_take.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.FINISH) {
            ll_take.setVisibility(VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_finished.setVisibility(VISIBLE);
            btn_pay.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.ALL_ORDER) {
            ll_take.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_logistics.setVisibility(GONE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
        }
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
