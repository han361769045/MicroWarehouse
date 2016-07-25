package com.zczczy.leo.microwarehouse.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.squareup.otto.Subscribe;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.items.TakeOrderItemView;
import com.zczczy.leo.microwarehouse.items.TakeOrderItemView_;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;
import com.zczczy.leo.microwarehouse.model.OrderModel;
import com.zczczy.leo.microwarehouse.model.PayResult;
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

/**
 * Created by Leo on 2016/5/6.
 */
@EActivity(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {

    @ViewById
    LinearLayout ll_shipping, ll_pre_order_item, ll_logistics, ll_next, ll_tracking_no, ll_take;

    @ViewById
    Button btn_canceled, btn_cancel_order, btn_logistics, btn_pay, btn_finish, btn_finished, btn_delete_order;

    @ViewById
    TextView tv_shipping, txt_phone, tv_shipping_address, txt_order_no, txt_sub_express_charges, txt_pay_total_rmb, txt_tracking_no;

    @StringRes
    String text_goods_price, text_shipping, text_take_shipping_address, text_order_no, text_tracking_no;

    @StringRes
    String text_take_order_time, text_pay_order_time, text_receiver_order_time, text_shipping_time, text_finish_time;

    @ViewById
    TextView txt_take_order_time, txt_pay_order_time, txt_receiver_order_time, txt_shipping_time, txt_pay_order_time_two, txt_finish_time;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @Extra
    String orderId;

    @Bean
    OttoBus bus;

    OrderModel mAppOrder;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
    }

    @AfterViews
    void afterView() {
        bus.register(this);
        AndroidTool.showLoadDialog(this);
        ll_next.setVisibility(View.GONE);
    }

    @Background
    void getOrderDetailById() {
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

            if (Constants.CASH == result.Data.MPaymentType) {
                txt_pay_order_time.setVisibility(View.GONE);
                txt_pay_order_time_two.setVisibility(View.VISIBLE);
                txt_pay_order_time_two.setText(String.format(text_pay_order_time, result.Data.PayTime));
            } else {
                txt_pay_order_time.setVisibility(View.VISIBLE);
                txt_pay_order_time.setText(String.format(text_pay_order_time, result.Data.PayTime));
                txt_pay_order_time_two.setVisibility(View.GONE);
            }
            txt_take_order_time.setText(String.format(text_take_order_time, result.Data.CreateTime));
            txt_receiver_order_time.setText(String.format(text_receiver_order_time, result.Data.DepotJdTime));
            txt_shipping_time.setText(String.format(text_shipping_time, result.Data.FhTime));


            txt_finish_time.setText(String.format(text_finish_time, result.Data.ShTime));
            if (result.Data.MorderStatus == Constants.DUEPAYMENT) {
                ll_take.setVisibility(View.VISIBLE);
                btn_cancel_order.setVisibility(View.VISIBLE);
                btn_pay.setVisibility(result.Data.MPaymentType == 2 ? View.GONE : View.VISIBLE);
                btn_canceled.setVisibility(View.GONE);
                btn_logistics.setVisibility(View.GONE);
                btn_finish.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
                btn_delete_order.setVisibility(View.VISIBLE);
            } else if (result.Data.MorderStatus == Constants.PAID) {
                ll_take.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
                btn_delete_order.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.SELECT_) {
                ll_take.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
                btn_delete_order.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.RECEIVER) {
                ll_take.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
                btn_delete_order.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.SHIPPING) {
                ll_take.setVisibility(View.VISIBLE);
//                ll_take.setVisibility((!btn_finish.isShown() && StringUtils.isEmpty(result.Data.TrackingNo)) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.VISIBLE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
                btn_delete_order.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.CONFIRM) {
                ll_take.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_finished.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
                btn_delete_order.setVisibility(View.GONE);
            } else if (result.Data.MorderStatus == Constants.FINISH) {
                ll_take.setVisibility(View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_finished.setVisibility(View.VISIBLE);
                btn_pay.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
                btn_delete_order.setVisibility(View.VISIBLE);
            } else if (result.Data.MorderStatus == Constants.CANCELED_ORDER) {
                ll_take.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(StringUtils.isEmpty(result.Data.TrackingNo) ? View.GONE : View.VISIBLE);
                btn_logistics.setVisibility(View.GONE);
                btn_finish.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
                btn_canceled.setVisibility(View.GONE);
                btn_delete_order.setVisibility(View.VISIBLE);
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
            btn_pay.setVisibility(View.GONE);
            btn_canceled.setVisibility(View.VISIBLE);
            setResult(RESULT_OK);
        }
    }

    @Click
    void btn_delete_order() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("提示").setMessage("确认删除订单？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AndroidTool.showLoadDialog(OrderDetailActivity.this);
                delOrderByOrderId();
            }
        }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
    }

    @Background
    void delOrderByOrderId() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterDelOrderByOrderId(myRestClient.delOrderByOrderId(mAppOrder.MOrderId));
    }

    @UiThread
    void afterDelOrderByOrderId(BaseModel result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            AndroidTool.showToast(this, "删除成功");
            setResult(RESULT_OK);
            finish();
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
        switch (mAppOrder.MPaymentType) {
            case Constants.CASH:
//                OrderDetailActivity_.intent(this).orderId(mAppOrder.MOrderId).start();
                break;
            case Constants.UM_PAY:
                UmspayActivity_.intent(this).order(mAppOrder).start();
                break;
            case Constants.ALI_PAY:
                aliPay(mAppOrder.AlipayInfo);
                break;
            case Constants.WEI_PAY:
                if (mAppOrder.WxPayData != null) {
                    mAppOrder.WxPayData.extData = mAppOrder.MOrderId;
                    app.iWXApi.sendReq(mAppOrder.WxPayData);
                }
                break;
        }
    }

    @Background
    public void aliPay(String payInfo) {
        PayTask aliPay = new PayTask(this);
        afterAliPay(aliPay.pay(payInfo, true));
    }

    @UiThread
    void afterAliPay(String result) {
        AndroidTool.dismissLoadDialog();
        PayResult payResult = new PayResult(result);
        /**
         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
         * docType=1) 建议商户依赖异步通知
         */
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        switch (resultStatus) {
            case "9000":
                AndroidTool.showToast(this, "支付成功");
                btn_pay.setVisibility(View.GONE);
                btn_finish.setVisibility(View.VISIBLE);
                setResult(RESULT_OK);
                break;
            case "8000":
                AndroidTool.showToast(this, "支付结果确认中");
                break;
            case "4000":
                AndroidTool.showToast(this, "订单支付失败");
                break;
            case "6001":
                AndroidTool.showToast(this, "用户中途取消");
                break;
            case "6002":
                AndroidTool.showToast(this, "网络连接出错");
                break;
            default: {
                AndroidTool.showToast(this, "网络连接出错");
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        AndroidTool.showLoadDialog(this);
        getOrderDetailById();
    }

    @Subscribe
    public void NotifyUI(PayResp resp) {
        switch (resp.errCode) {
            case 0:
                AndroidTool.showLoadDialog(this);
                getOrderDetailById();
                break;
            case -1:
                AndroidTool.showToast(this, "支付异常");
                break;
            case -2:
                AndroidTool.showToast(this, "您取消了支付");
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        bus.unregister(this);
    }


}
