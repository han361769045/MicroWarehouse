package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.LogisticsInfoActivity_;
import com.zczczy.leo.microwarehouse.activities.MemberOrderActivity;
import com.zczczy.leo.microwarehouse.activities.OrderDetailActivity_;
import com.zczczy.leo.microwarehouse.activities.UmspayActivity_;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;
import com.zczczy.leo.microwarehouse.model.OrderModel;
import com.zczczy.leo.microwarehouse.model.PayResult;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
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
    Button btn_canceled, btn_cancel_order, btn_logistics, btn_pay, btn_finish, btn_finished, btn_delete_order;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @Pref
    MyPrefs_ pre;

    @App
    MyApplication app;

    @StringRes
    String no_net, text_count, text_goods_price;

    MemberOrderActivity memberOrderActivity;

    public MemberOrderItemView(Context context) {
        super(context);
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
        switch (_data.MPaymentType) {
            case Constants.CASH:
                OrderDetailActivity_.intent(memberOrderActivity).orderId(_data.MOrderId).startForResult(1000);
                break;
            case Constants.UM_PAY:
                UmspayActivity_.intent(memberOrderActivity).order(_data).startForResult(1000);
                break;
            case Constants.ALI_PAY:
                aliPay(_data.AlipayInfo);
                break;
            case Constants.WEI_PAY:
                if (_data.WxPayData != null) {
                    _data.WxPayData.extData = _data.MOrderId;
                    app.iWXApi.sendReq(_data.WxPayData);
                }
                break;
        }
    }

    @Background
    public void aliPay(String payInfo) {
        PayTask aliPay = new PayTask(memberOrderActivity);
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
                AndroidTool.showToast(context, "支付成功");
                baseUltimateRecyclerViewAdapter.getItems().remove(_data);
                baseUltimateRecyclerViewAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                break;
            case "8000":
                AndroidTool.showToast(context, "支付结果确认中");
                break;
            case "4000":
                AndroidTool.showToast(context, "订单支付失败");
                break;
            case "6001":
                AndroidTool.showToast(context, "用户中途取消");
                break;
            case "6002":
                AndroidTool.showToast(context, "网络连接出错");
                break;
            default: {
                AndroidTool.showToast(context, "网络连接出错");
            }
        }
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
            btn_pay.setVisibility(_data.MPaymentType == 2 ? GONE : VISIBLE);
            btn_canceled.setVisibility(GONE);
            btn_logistics.setVisibility(GONE);
            btn_finish.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
            btn_delete_order.setVisibility(VISIBLE);
        } else if (_data.MorderStatus == Constants.PAID) {
            ll_take.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
            btn_delete_order.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.SELECT_) {
            ll_take.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
            btn_delete_order.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.RECEIVER) {
            ll_take.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
            btn_delete_order.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.SHIPPING) {
            ll_take.setVisibility(VISIBLE);
            btn_finish.setVisibility(View.VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_cancel_order.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
            btn_delete_order.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.CONFIRM) {
            ll_take.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
            btn_delete_order.setVisibility(GONE);
        } else if (_data.MorderStatus == Constants.FINISH) {
            ll_take.setVisibility(VISIBLE);
            btn_logistics.setVisibility(StringUtils.isEmpty(_data.TrackingNo) ? GONE : VISIBLE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_finished.setVisibility(VISIBLE);
            btn_pay.setVisibility(GONE);
            btn_canceled.setVisibility(GONE);
            btn_delete_order.setVisibility(VISIBLE);
        } else if (_data.MorderStatus == Constants.CANCELED_ORDER) {
            ll_take.setVisibility(VISIBLE);
            btn_logistics.setVisibility(GONE);
            btn_finish.setVisibility(GONE);
            btn_cancel_order.setVisibility(GONE);
            btn_finished.setVisibility(GONE);
            btn_pay.setVisibility(GONE);
            btn_canceled.setVisibility(VISIBLE);
            btn_delete_order.setVisibility(VISIBLE);
        }
    }

    @Click
    void btn_delete_order() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle("提示").setMessage("确认删除订单？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AndroidTool.showLoadDialog(context);
                delOrderByOrderId();
            }
        }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
    }

    @Background
    void delOrderByOrderId() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterDelOrderByOrderId(myRestClient.delOrderByOrderId(_data.MOrderId));
    }

    @UiThread
    void afterDelOrderByOrderId(BaseModel result) {
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


    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
