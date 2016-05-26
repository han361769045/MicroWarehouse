package com.zczczy.leo.microwarehouse.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.items.TakeOrderItemView;
import com.zczczy.leo.microwarehouse.items.TakeOrderItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;
import com.zczczy.leo.microwarehouse.model.OrderModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2016/5/6.
 */
@EActivity(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {

    @ViewById
    LinearLayout ll_shipping, ll_pre_order_item, ll_logistics, ll_next, ll_tracking_no, ll_take;

    @ViewById
    Button btn_canceled, btn_cancel_order, btn_logistics, btn_pay, btn_finish, btn_finished;

    @ViewById
    TextView tv_shipping, txt_phone, tv_shipping_address, txt_order_no, txt_sub_express_charges, txt_pay_total_rmb, txt_tracking_no;

    @StringRes
    String text_goods_price, text_shipping, text_take_shipping_address, text_order_no, text_tracking_no;

    @StringRes
    String text_take_order_time, text_pay_order_time, text_receiver_order_time, text_shipping_time, text_finish_time;

    @ViewById
    TextView txt_take_order_time, txt_pay_order_time, txt_receiver_order_time, txt_shipping_time, txt_finish_time;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @Extra
    String orderId;

    OrderModel mAppOrder;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        AndroidTool.showLoadDialog(this);
        ll_next.setVisibility(View.GONE);
    }

    @Background
    void getOrderDetailById() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterGetOrderDetailById(myRestClient.getOrderInfoById(orderId));
    }

    @UiThread
    void afterGetOrderDetailById(BaseModelJson<OrderModel> result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            mAppOrder = result.Data;
            tv_shipping.setText(String.format(text_shipping, result.Data.ShrName));
            txt_phone.setText(result.Data.Lxdh);
            tv_shipping_address.setText(String.format(text_take_shipping_address, result.Data.DetailAddress));
            ll_pre_order_item.removeAllViews();
            for (OrderDetailModel orderDetailModel : result.Data.MOrderDetailList) {
                TakeOrderItemView preOrderItemView = TakeOrderItemView_.build(this);
                preOrderItemView.init(orderDetailModel);
                ll_pre_order_item.addView(preOrderItemView);
            }
            txt_order_no.setText(String.format(text_order_no, "  " + result.Data.MOrderNo));
            txt_sub_express_charges.setText(String.format(text_goods_price, result.Data.Postage));
            txt_pay_total_rmb.setText(String.format(text_goods_price, result.Data.MOrderMoney));
            ll_tracking_no.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
            txt_tracking_no.setText(String.format(text_tracking_no, result.Data.TrackingNo));
            ll_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
            txt_take_order_time.setText(String.format(text_take_order_time, result.Data.CreateTime));
            txt_pay_order_time.setText(String.format(text_pay_order_time, result.Data.PayTime));
            txt_receiver_order_time.setText(String.format(text_receiver_order_time, result.Data.DepotJdTime));
            txt_shipping_time.setText(String.format(text_shipping_time, result.Data.FhTime));
            txt_finish_time.setText(String.format(text_finish_time, result.Data.ShTime));
            if (result.Data.MorderStatus == Constants.DUEPAYMENT) {
                ll_take.setVisibility(View.VISIBLE);
                btn_cancel_order.setVisibility(View.VISIBLE);
                btn_pay.setVisibility(result.Data.MPaymentType == 1 ? View.VISIBLE : View.GONE);
                btn_canceled.setVisibility(View.GONE);
                btn_logistics.setVisibility(View.GONE);
                btn_finish.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.PAID) {
                ll_take.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.SELECT_) {
                ll_take.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.RECEIVER) {
                ll_take.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.SHIPPING) {
                ll_take.setVisibility(View.VISIBLE);
//                ll_take.setVisibility((!btn_finish.isShown() && StringUtils.isEmpty(result.Data.TrackingNo)) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.VISIBLE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.CONFIRM) {
                ll_take.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.FINISH) {
                ll_take.setVisibility(View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_finished.setVisibility(View.VISIBLE);
                btn_pay.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.ALL_ORDER) {
                ll_take.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(View.GONE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
            }
        }
    }

    @Click
    void btn_cancel_order() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("提示").setMessage("确认取消订单？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AndroidTool.showLoadDialog(OrderDetailActivity.this);
                cancelOrder();
            }
        }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
    }

    @Background
    void cancelOrder() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterCancelOrder(myRestClient.cancelOrderByOrderId(mAppOrder.MOrderId));
    }

    @UiThread
    void afterCancelOrder(BaseModel result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);

        } else {
            btn_cancel_order.setVisibility(View.GONE);
            setResult(RESULT_OK);
        }
    }

    @Click
    void btn_finish() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("提示").setMessage("确认收货？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AndroidTool.showLoadDialog(OrderDetailActivity.this);
                confirmReceipt();
            }
        }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
    }

    @Background
    void confirmReceipt() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterConfirm(myRestClient.confirmSh(mAppOrder.MOrderId));
    }

    @UiThread
    void afterConfirm(BaseModel bm) {
        AndroidTool.dismissLoadDialog();
        if (bm == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!bm.Successful) {
            AndroidTool.showToast(this, bm.Error);
        } else {
            btn_finish.setVisibility(View.GONE);
            setResult(RESULT_OK);
        }
    }

    @Click
    void ll_logistics() {
        LogisticsInfoActivity_.intent(this).MOrderId(mAppOrder.MOrderId).start();

    }

    @Click
    void btn_logistics() {
        LogisticsInfoActivity_.intent(this).MOrderId(mAppOrder.MOrderId).start();
    }

    @Click
    void btn_pay() {
        UmspayActivity_.intent(this).order(mAppOrder).start();
//        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderDetailById();
    }
}
