package com.zczczy.leo.microwarehouse.tools;

/**
 * Created by Leo on 2016/5/21.
 */
public class Constants {
    //测试环境
    public static final String ROOT_URL = "http://218.61.203.50:8018/ContentView/";
    //    public static final String ROOT_URL = "http://192.168.0.198:8018/ContentView/";
    public static final String PAY_URL = "http://116.228.21.162:9127/umsFrontWebQmjf/umspay";

    //正式环境
//    public static final String PAY_URL = "https://mpos.quanminfu.com:8018/umsFrontWebQmjf/umspay";
//    public static final String ROOT_URL = "http://wcapia.zczczy.com/ContentView/";

    // 商户PID 以2088开头的16位纯数字
    public static final String PARTNER = "2088511343434684";
    // 商户收款账号
    public static final String SELLER = "zczczy2014@sina.com";
    // 商户私钥，pkcs8格式 自助生成//商户私钥，自助生成，即rsa_private_key.pem中去掉首行，最后一行，空格和换行最后拼成一行的字符串，rsa_private_key.pem这个文件等你申请支付宝签约成功后，按照文档说明你会生成的.........................这里要用PKCS8格式用户私钥，不然调用不会成功的，那个格式你到时候会生成的，表急。
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALuZvsiGpA155FtHVPScL8IM5353k1vGBNJ35jsi74LkCC0j0pNsSsn2bFRAZElX7uBuk1UH9KYNaiiGjx+l0U5ursPwTu7/LTEaMpiQ1xFpOxuUbzPSAM13RpnxCEvjYOrEr4DoMxy33fPH1KT+x1RT4FZAOCOvLsXPlznIBYdlAgMBAAECgYEAiaB2DUhxbA2DUM+Y7Mb0ZmjvH6F7srUn7nvVpIaxrZW6xdrk7hBhIZ8tw/VlXUUsUuvsHFJ00ak/uzFUKISOcriSTs/TQ/EGNLqki12ebwdXulHPK5r269hWI5gzc4E2odwQFcMHRtMtKiHmz8X5+bIV4EGvQYGkXeB5mXa5HwECQQDqJbStkp4vGw05Bovg2hosCoLMYpBppkCTN+//PCSGejtIqT6LNqijVsNcBC8HJiyjR6McVKkHF/sJ0oW8IzwlAkEAzRvvks5bR4LFqCOe55Pl3btz5o55ykJUdFDhbLscg2GMr0jNIwtBq4nL6gnyVD8VYjXfuaOvN0nnuneTxcGaQQJAd/DWq68WTqT50gNTddvjWjoMJGw885GRNJiL3N/PCf/lDGR9wExAeGNCcYlHGc9pQD67BBEuAI89LhvkG6qk7QJBAKzYad/RPmLY9TBoNDT7Ea71lQop7sBffUQ4FH/IfpLI1gGw3A44IjMogErN7wTH1IFa3RpXsgRpp3of4su4BwECQQDh0+VMCuW04RqVJzL4NVS/GULnHATbDaW8TjtjqD41AYIYpw1//eJd0/N9zotfSdAhyYYb7Q6LG9NztngqlZ6A";
    // 支付宝公钥 ,demo自带不用改，或者用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取；或者文档上也有。
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";


    public static final String ANDROID = "1";//请求类型  android

    public static final int PAGE_COUNT = 10;

    public static final int MOBILE_LOGIN = 1; //登录类型（1：手机验证码登录，2：账号密码登录）
    public static final int NORMAL_LOGIN = 2; //登录类型（1：手机验证码登录，2：账号密码登录）

    public static final int LOING_CODE = 2; //1：用户注册，2：用户登录
    public static final int REGISTER_CODE = 1; //1：用户注册，2：用户登录

    public static final int UM_PAY = 1; //支付方式（1网银，2货到付款）
    public static final int CASH = 2; //支付方式（1网银，2货到付款）
    public static final int ALI_PAY = 3; //支付方式（1网银，2货到付款 3支付宝  4微信）
    public static final int WEI_PAY = 4; //支付方式（1网银，2货到付款）

    public static final String HOME_AD = "1";

    public static final String GOODS_TYPE = "1";  //跳转标识(1:商品类别页，2：商品明细)
    public static final String GOODS_DETAIL = "2"; //跳转标识(1:商品类别页，2：商品明细)


    public static final String ZONG_HE = "1"; //1:综合排序，2：销量降序，3：价格降序，4：价格升序
    public static final String SELL_COUNT = "2"; //1:综合排序，2：销量降序，3：价格降序，4：价格升序
    public static final String PRICE_DESC = "3"; //1:综合排序，2：销量降序，3：价格降序，4：价格升序
    public static final String PRICE_ASC = "4"; //1:综合排序，2：销量降序，3：价格降序，4：价格升序


    public static final int Goods_UP = 1; //1：上架，0：下架
    public static final int Goods_DOWN = 0; //1：上架，0：下架


    public static final int DUEPAYMENT = 0; //0:待支付
    public static final int PAID = 1;   //1：已支付
    public static final int SELECT_ = 2; //2：仓库选择
    public static final int RECEIVER = 3; //3：仓库接单
    public static final int SHIPPING = 4; //4:已发货
    public static final int CONFIRM = 5; //5:确认收货
    public static final int FINISH = 6; //5:交易完成
    public static final int ALL_ORDER = 9; //9.取消订单

}
