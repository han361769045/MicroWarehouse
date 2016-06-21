package com.zczczy.leo.microwarehouse.activities;

import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.items.TakeOrderItemView;
import com.zczczy.leo.microwarehouse.items.TakeOrderItemView_;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;
import com.zczczy.leo.microwarehouse.model.OrderModel;
import com.zczczy.leo.microwarehouse.model.ShippingAddressModel;
import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;
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
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2016/5/24.
 */
@EActivity(R.layout.activity_take_order)
public class TakeOrderActivity extends BaseActivity {

    @ViewById
    LinearLayout ll_shipping, ll_store, ll_pre_order_item;

    @ViewById
    RelativeLayout rl_postal, rl_comment;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @Bean
    MyBackgroundTask mMyBackgroundTask;

    @ViewById
    TextView tv_shipping, txt_phone, tv_shipping_address, txt_sub_express_charges, txt_comment, txt_total;

    @ViewById
    RadioButton rb_umpay, rb_cash, rb_bao_pay, rb_wechat_pay;

    @StringRes
    String text_goods_price, text_shipping, text_take_shipping_address, text_comment;

    @Extra
    String ids;

    @Bean
    OttoBus bus;

    @Extra
    OrderModel model;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        bus.register(this);
        AndroidTool.showLoadDialog(this);
        //立即购买时直接加载数据
        if (StringUtils.isEmpty(ids)) {
            BaseModelJson<OrderModel> result = new BaseModelJson<>();
            result.Data = model;
            result.Successful = true;
            afterCreateTempOrderInfo(result);
        } else {
            createTempOrderInfo();
        }
    }


    @Background
    void createTempOrderInfo() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        Map<String, String> map = new HashMap<>(1);
        map.put("BuyCardIds", ids);
        afterCreateTempOrderInfo(myRestClient.createTempOrder(map));
    }

    @UiThread
    void afterCreateTempOrderInfo(BaseModelJson<OrderModel> result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            finish();
            AndroidTool.showToast(this, result.Error);
        } else {
            model = result.Data;
            rb_umpay.setChecked(Constants.UM_PAY == model.MPaymentType);
            rb_cash.setChecked(Constants.CASH == model.MPaymentType);
            rb_bao_pay.setChecked(Constants.ALI_PAY == model.MPaymentType);
            rb_wechat_pay.setChecked(Constants.WEI_PAY == model.MPaymentType);
            ShippingAddressModel model = new ShippingAddressModel();
            if (StringUtils.isEmpty(result.Data.ShrName) || StringUtils.isEmpty(result.Data.Lxdh) || StringUtils.isEmpty(result.Data.DetailAddress)) {
                model = null;
            } else {
                model.DetailAddress = result.Data.DetailAddress;
                model.ProvinceName = result.Data.ProvinceName;
                model.CityName = result.Data.CityName;
                model.AreaName = result.Data.AreaName;
                model.ReceiptName = result.Data.ShrName;
                model.Mobile = result.Data.Lxdh;
            }
            setShipping(model);
            txt_sub_express_charges.setText(String.format(text_goods_price, result.Data.Postage));
            txt_total.setText(String.format(text_goods_price, result.Data.MOrderMoney));
            for (OrderDetailModel orderDetailModel : result.Data.MOrderDetailList) {
                TakeOrderItemView preOrderItemView = TakeOrderItemView_.build(this);
                preOrderItemView.init(orderDetailModel);
                ll_pre_order_item.addView(preOrderItemView);
            }

        }
    }

    //设置 收货地址
    void setShipping(ShippingAddressModel model) {
        if (model != null) {
            tv_shipping.setText(String.format(text_shipping, model.ReceiptName));
            txt_phone.setText(model.Mobile);
            tv_shipping_address.setText(String.format(text_take_shipping_address, model.ProvinceName + model.CityName + model.AreaName + model.DetailAddress));
        } else {
            tv_shipping.setText("!填写收货地址");
        }
    }


    @Click
    void ll_shipping() {
        ShippingAddressActivity_.intent(this).isFinish(true).startForResult(1000);
    }

    @OnActivityResult(1000)
    void onSelectShippingAddress(int resultCode, @OnActivityResult.Extra ShippingAddressModel model) {
        if (resultCode == 1001) {
            this.model.AreaId = model.AreaId;
            this.model.ShrName = model.ReceiptName;
            this.model.Lxdh = model.Mobile;
            this.model.DetailAddress = model.DetailAddress;
            setShipping(model);
        }
    }

    @Click
    void txt_take() {
        if (AndroidTool.checkTextViewIsNull(tv_shipping, txt_phone, tv_shipping_address)) {
            AndroidTool.showToast(this, "请选择收货地址");
        } else {
            AndroidTool.showLoadDialog(this);
            if (rb_umpay.isChecked()) {
                model.MPaymentType = Constants.UM_PAY;
            } else if (rb_bao_pay.isChecked()) {
                model.MPaymentType = Constants.ALI_PAY;
            } else if (rb_wechat_pay.isChecked()) {
                model.MPaymentType = Constants.WEI_PAY;
            } else {
                model.MPaymentType = Constants.CASH;
            }
            model.MarkInfo = txt_comment.getText().toString().trim();
            takeOrder();
        }
    }

    @Background
    void takeOrder() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterTakeOrder(myRestClient.createOrder(model));
    }

    @UiThread
    void afterTakeOrder(BaseModelJson<OrderModel> result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            switch (result.Data.MPaymentType) {
                case Constants.CASH:
                    OrderDetailActivity_.intent(this).orderId(result.Data.MOrderId).start();
                    finish();
                    break;
                case Constants.UM_PAY:
                    UmspayActivity_.intent(this).order(result.Data).start();
                    finish();
                    break;
                case Constants.ALI_PAY:
                    mMyBackgroundTask.aliPay(result.Data.AlipayInfo, this, result.Data.MOrderId);
                    break;
                case Constants.WEI_PAY:
                    if (result.Data.WxPayData != null) {
                        result.Data.WxPayData.extData = result.Data.MOrderId;
                        app.iWXApi.sendReq(result.Data.WxPayData);
                    }
                    break;
            }
        }
    }

    @Click
    void rl_comment() {
        ChangeInfoActivity_.intent(this).content(text_comment).flagT("5").startForResult(1000);
    }

    @OnActivityResult(1000)
    void onComment(int resultCode, @OnActivityResult.Extra String contents) {
        if (resultCode == 1005) {
            txt_comment.setText(contents);
        }
    }

    @Subscribe
    public void NotifyUI(PayResp resp) {
        switch (resp.errCode) {
            case 0:
                break;
            case -1:
                AndroidTool.showToast(this, "支付异常");
                break;
            case -2:
                AndroidTool.showToast(this, "您取消了支付");
                break;
        }
        OrderDetailActivity_.intent(this).orderId(resp.extData).start();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        bus.unregister(this);
    }
}
