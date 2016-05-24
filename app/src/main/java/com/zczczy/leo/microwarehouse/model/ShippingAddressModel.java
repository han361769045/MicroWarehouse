package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * Created by Leo on 2016/5/21.
 */
public class ShippingAddressModel implements Serializable {

    /**
     * MReceiptAddressId : 1
     * AreaId : 2
     * UserInfoId : sample string 3
     * ReceiptName : sample string 4
     * DetailAddress : sample string 5
     * IsPrimary : sample string 6
     * Mobile : sample string 7
     * CityId : 8
     * ProvinceId : 9
     * ProvinceName : sample string 10
     * CityName : sample string 11
     * AreaName : sample string 12
     */

    public int MReceiptAddressId; //收货地址主键ID
    public int AreaId; //区ID
    public String UserInfoId; //用户主键ID
    public String ReceiptName; //收货人
    public String DetailAddress;//   详细地址

    public String IsPrimary; //是否是主收货地址（0：否，1：是）
    public String Mobile; //手机号
    public String Coordinates; //位置坐标

    public int CityId; //城市ID
    public int ProvinceId; //省ID
    public String ProvinceName; //省名称
    public String CityName; //城市名称
    public String AreaName; //区域名称
}
