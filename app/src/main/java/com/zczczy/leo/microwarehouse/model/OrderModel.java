package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leo on 2016/5/23.
 */
public class OrderModel implements Serializable {


    /**
     * BuyCardIds : sample string 1
     * MOrderId : sample string 2
     * DepotId : sample string 3
     * UserInfoId : sample string 4
     * LogisticsInfoId : 5
     * MOrderNo : sample string 6
     * CreateTime : sample string 7
     * MOrderMoney : 8.0
     * MPaymentType : 9
     * PsType : 10
     * AreaId : 11
     * ShrName : sample string 12
     * Lxdh : sample string 13
     * DetailAddress : sample string 14
     * Coordinates : sample string 15
     * chrCode : sample string 16
     * transId : sample string 17
     * merSign : sample string 18
     * MorderStatus : 19
     * GoodsAllCount : 20
     * TrackingNo : sample string 21
     * Postage : 22.0
     * PayTime : sample string 23
     * SelDepotTime : sample string 24
     * DepotJdTime : sample string 25
     * FhTime : sample string 26
     * ShTime : sample string 27
     * MarkInfo : sample string 28
     */

    public String BuyCardIds; //购物车选择的记录ID
    public String MOrderId; //订单主键ID
    public String DepotId; //发货仓库ID
    public String UserInfoId; //用户主键ID
    public int LogisticsInfoId; //物流主键ID
    public String MOrderNo; //订单编号
    public String CreateTime; //订单创建时间
    public double MOrderMoney; //订单总金额
    public int MPaymentType; //支付方式（1网银，2货到付款）
    public int PsType; //配送方式(1:邮寄，2：线下配送)
    public int AreaId; //区ID
    public String ProvinceName; //区域名称
    public String CityName; //城市名称
    public String AreaName; //省名称
    public String ShrName; //收货人姓名
    public String Lxdh; //联系电话
    public String DetailAddress; //详细地址
    public String Coordinates; //地理坐标
    public String chrCode; //银联商户信息
    public String MerchantId; //银联特征码
    public String transId;  //银联编号
    public String merSign; //银联签名
    public int MorderStatus; //订单状态(0:待支付，1：已支付，2：仓库选择，3：仓库接单，4：已发货，5：确认收货，6：交易完成，9：取消订单)
    public String MorderStatusDisp; //订单状态描述
    public int GoodsAllCount; //商品总数量
    public String TrackingNo; //运单号
    public double Postage; //邮费合计
    public String PayTime; //支付时间
    public String SelDepotTime; //客服选择仓库时间
    public String DepotJdTime; //仓库接单时间
    public String FhTime; //发货时间
    public String ShTime; //确认收货时间
    public String MarkInfo; //备注说明

    public List<OrderDetailModel> MOrderDetailList; //订单明细
}
