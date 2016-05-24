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

    public String BuyCardIds;
    public String MOrderId;
    public String DepotId;
    public String UserInfoId;
    public int LogisticsInfoId;
    public String MOrderNo;
    public String CreateTime;
    public double MOrderMoney;
    public int MPaymentType;
    public int PsType;
    public int AreaId;
    public String ProvinceName;
    public String CityName;
    public String AreaName;
    public String ShrName;
    public String Lxdh;
    public String DetailAddress;
    public String Coordinates;
    public String chrCode;
    public String transId;
    public String merSign;
    public int MorderStatus;
    public String MorderStatusDisp;
    public int GoodsAllCount;
    public String TrackingNo;
    public double Postage;
    public String PayTime;
    public String SelDepotTime;
    public String DepotJdTime;
    public String FhTime;
    public String ShTime;
    public String MarkInfo;
    public String MerchantId;

    public List<OrderDetailModel> MOrderDetailList;
}
