package com.zczczy.leo.microwarehouse.activities;

import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.items.TakeOrderItemView;
import com.zczczy.leo.microwarehouse.items.TakeOrderItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;
import com.zczczy.leo.microwarehouse.model.OrderModel;
import com.zczczy.leo.microwarehouse.model.PayResult;
import com.zczczy.leo.microwarehouse.model.ShippingAddressModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.tools.SignUtils;

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
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

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

    @ViewById
    TextView tv_shipping, txt_phone, tv_shipping_address, txt_sub_express_charges, txt_comment, txt_total;

    @ViewById
    RadioButton rb_umpay, rb_cash, rb_bao_pay, rb_wechat_pay;

    @StringRes
    String text_goods_price, text_shipping, text_take_shipping_address, text_comment;

    @Extra
    String ids;

    IWXAPI api;

    OrderModel model;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        AndroidTool.showLoadDialog(this);
        createTempOrderInfo();
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
//            test();
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
                    alipay(result.Data.AlipayInfo);
                    break;
                case Constants.WEI_PAY:
                    break;
            }

//            if (Constants.CASH == result.Data.MPaymentType) {
//                OrderDetailActivity_.intent(this).orderId(result.Data.MOrderId).start();
//            } else if (Constants.UM_PAY) {
//                UmspayActivity_.intent(this).order(result.Data).start();
//            }
        }
    }

    @Click
    void rl_comment() {
        ChangeInfoActivity_.intent(this).content(text_comment).flagT("5").startForResult(1000);
    }

    @OnActivityResult(1000)
    void onCommnet(int resultCode, @OnActivityResult.Extra String contents) {
        if (resultCode == 1005) {
            txt_comment.setText(contents);
        }
    }

    public void test() {
        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
        alipay(payInfo);
    }

    @Background
    void alipay(String payInfo) {
        PayTask alipay = new PayTask(TakeOrderActivity.this);
        afterAlipay(alipay.pay(payInfo, true));
    }

    @UiThread
    void afterAlipay(String ss) {
        AndroidTool.dismissLoadDialog();
        PayResult payResult = new PayResult(ss);
        /**
         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
         * docType=1) 建议商户依赖异步通知
         */
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息

        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(resultStatus, "9000")) {
            Toast.makeText(TakeOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
        } else {
            // 判断resultStatus 为非"9000"则代表可能支付失败
            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                Toast.makeText(TakeOrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                Toast.makeText(TakeOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

            }
        }
    }


    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Constants.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Constants.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, Constants.RSA_PRIVATE);
    }


    void testWxPay() {
        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
        String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
        api.registerApp("wxb4ba3c02aa476ea1");
//        try{
//            byte[] buf = Util.httpGet(url);
//            if (buf != null && buf.length > 0) {
//                String content = new String(buf);
//                Log.e("get server pay params:",content);
//                JSONObject json = new JSONObject(content);
//                if(null != json && !json.has("retcode") ){
//                    PayReq req = new PayReq();
//                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
//                    req.appId			= json.getString("appid");
//                    req.partnerId		= json.getString("partnerid");
//                    req.prepayId		= json.getString("prepayid");
//                    req.nonceStr		= json.getString("noncestr");
//                    req.timeStamp		= json.getString("timestamp");
//                    req.packageValue	= json.getString("package");
//                    req.sign			= json.getString("sign");
//                    req.extData			= "app data"; // optional
//                    Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
//                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                    api.sendReq(req);
//                }else{
//                    Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
//                    Toast.makeText(PayActivity.this, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
//                }
//            }else{
//                Log.d("PAY_GET", "服务器请求错误");
//                Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
//            }
//        }catch(Exception e){
//            Log.e("PAY_GET", "异常："+e.getMessage());
//            Toast.makeText(PayActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
    }


}
