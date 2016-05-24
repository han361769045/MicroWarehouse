package com.zczczy.leo.microwarehouse.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
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
    LinearLayout ll_next;

//    @ViewById
//    RelativeLayout ll_lb, ll_pay, ll_should_pay, ll_paid, rl_express_charges;

    @ViewById
    Button btn_canceled, btn_cancel_order, btn_logistics, btn_pay, btn_finish, btn_finished;

//    @StringRes
//    String text_order_no, txt_shipping_address, dian_balance, home_rmb, home_lb, txt_shipping, text_count;

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
        getOrderDetailById();
    }

    @Background
    void getOrderDetailById() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterGetOrderDetailById(myRestClient.getOrderDetailById(orderId));
    }

    @UiThread
    void afterGetOrderDetailById(BaseModelJson<OrderModel> bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!bmj.Successful) {
            AndroidTool.showToast(this, bmj.Error);
        } else {
//            mAppOrder = bmj.Data;
//            txt_order_no.setText(String.format(text_order_no, "  " + bmj.Data.MOrderNo));
//            tv_shipping.setText(String.format(txt_shipping, bmj.Data.ShrName));
//            txt_phone.setText(bmj.Data.Lxdh);
//            tv_shipping_address.setText(String.format(txt_shipping_address, bmj.Data.DetailAddress));
//            txt_store.setText(bmj.Data.StoreName);
//            txt_sub_express_charges.setText(String.format(home_rmb, bmj.Data.Postage));
//            txt_pay_total_rmb.setText(String.format(home_rmb, bmj.Data.MOrderMoney));
//            txt_total_lb.setText(String.format(home_lb, bmj.Data.MOrderLbCount));

//            //设置返券内容
//            String temp = "";
//            if (bmj.Data.FanQuan != null) {
//                if (bmj.Data.FanQuan.RetMianZhi7 > 0) {
//                    temp += bmj.Data.FanQuan.RetMianZhi7Str + "*" + bmj.Data.FanQuan.RetMianZhi7 + "+";
//                }
//                if (bmj.Data.FanQuan.RetMianZhi8 > 0) {
//                    temp += bmj.Data.FanQuan.RetMianZhi8Str + "*" + bmj.Data.FanQuan.RetMianZhi8 + "+";
//                }
//                if (bmj.Data.FanQuan.RetMianZhi10 > 0) {
//                    temp += bmj.Data.FanQuan.RetMianZhi10Str + "*" + bmj.Data.FanQuan.RetMianZhi10 + "+";
//                }
//                if (bmj.Data.FanQuan.RetMianZhi9 > 0) {
//                    temp += bmj.Data.FanQuan.RetMianZhi9Str + "*" + bmj.Data.FanQuan.RetMianZhi9 + "+";
//                }
//                if (StringUtils.isEmpty(temp)) {
//                    temp = "不返券+";
//                }
//            } else {
//                temp = "不返券+";
//            }
//            txt_coupon.setText(temp.substring(0, temp.lastIndexOf('+')));
//            if (bmj.Data.MorderStatus == MyApplication.DUEPAYMENT) {
//                btn_cancel_order.setVisibility(View.GONE);
//                btn_pay.setVisibility(View.VISIBLE);
//                btn_logistics.setVisibility(View.GONE);
//                btn_finish.setVisibility(View.GONE);
//                btn_canceled.setVisibility(View.GONE);
//                //还需支付
//                ll_should_pay.setVisibility(View.VISIBLE);
//                ll_paid.setVisibility(View.VISIBLE);
//                if (bmj.Data.MOrderDzb >= 0) {
//                    txt_should_pay_l_rmb.setText(String.format(home_rmb, bmj.Data.MOrderMoney - bmj.Data.MOrderDzb));
//                    txt_paid_rmb.setText(String.format(home_rmb, bmj.Data.MOrderDzb));
//                }
//                rl_express_charges.setVisibility(View.GONE);
//            } else if (bmj.Data.MorderStatus == MyApplication.PAID) {
//                ll_take.setVisibility("1".equals(bmj.Data.GoodsType) ? View.GONE : View.VISIBLE);
//                btn_logistics.setVisibility(View.VISIBLE);
//                btn_finish.setVisibility(View.GONE);
//                btn_cancel_order.setVisibility(View.GONE);
//                btn_pay.setVisibility(View.GONE);
//                btn_canceled.setVisibility(View.GONE);
//                ll_should_pay.setVisibility(View.GONE);
//            } else if (bmj.Data.MorderStatus == MyApplication.CANCEL) {
//                btn_canceled.setVisibility(View.VISIBLE);
//                btn_logistics.setVisibility(View.GONE);
//                btn_finish.setVisibility(View.GONE);
//                btn_cancel_order.setVisibility(View.GONE);
//                btn_pay.setVisibility(View.GONE);
//            } else if (bmj.Data.MorderStatus == MyApplication.SEND) {
//                btn_logistics.setVisibility(View.VISIBLE);
//                btn_finish.setVisibility(View.VISIBLE);
//                btn_cancel_order.setVisibility(View.GONE);
//                btn_pay.setVisibility(View.GONE);
//                btn_canceled.setVisibility(View.GONE);
//                ll_should_pay.setVisibility(View.GONE);
//            } else if (bmj.Data.MorderStatus == MyApplication.CONFIRM) {
//                btn_logistics.setVisibility(View.VISIBLE);
//                btn_finish.setVisibility(View.GONE);
//                btn_cancel_order.setVisibility(View.GONE);
//                btn_pay.setVisibility(View.GONE);
//                btn_canceled.setVisibility(View.GONE);
//                ll_should_pay.setVisibility(View.GONE);
//            } else if (bmj.Data.MorderStatus == MyApplication.FINISH) {
//                btn_logistics.setVisibility(View.VISIBLE);
//                btn_finished.setVisibility(View.VISIBLE);
//                btn_finish.setVisibility(View.GONE);
//                btn_cancel_order.setVisibility(View.GONE);
//                btn_pay.setVisibility(View.GONE);
//                btn_canceled.setVisibility(View.GONE);
//                ll_should_pay.setVisibility(View.GONE);
//            }
//            ll_shipping.setVisibility("1".equals(bmj.Data.GoodsType) ? View.GONE : View.VISIBLE);
//            ll_logistics.setVisibility("1".equals(bmj.Data.GoodsType) ? View.GONE : View.VISIBLE);
//            rl_express_charges.setVisibility("1".equals(bmj.Data.GoodsType) ? View.GONE : View.VISIBLE);
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
//        myRestClient.setHeader("Token", pre.token().get());
//        myRestClient.setHeader("ShopToken", pre.shopToken().get());
//        myRestClient.setHeader("Kbn", MyApplication.ANDROID);
//        Map<String, String> map = new HashMap<>(1);
//        map.put("MOrderId", orderId);
//        afterConfirm(myRestClient.confirmReceipt(map));
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
        }
    }

    @Click
    void ll_logistics() {
//        LogisticsInfoActivity_.intent(this).MOrderId(mAppOrder.MOrderId).start();

    }

    @Click
    void btn_logistics() {
//        LogisticsInfoActivity_.intent(this).MOrderId(mAppOrder.MOrderId).start();
    }

    @Click
    void btn_pay() {
//        UnionPay order = new UnionPay();
//        order.ChrCode = mAppOrder.chrCode;
//        order.MerSign = mAppOrder.merSign;
//        order.TransId = mAppOrder.transId;
//        UmspayActivity_.intent(this).MOrderId(mAppOrder.MOrderId).order(order).start();
//        finish();
    }


}
