package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * Created by Leo on 2016/5/23.
 */
public class OrderDetailModel implements Serializable {


    /**
     * MOrderDetailId : sample string 1
     * MOrderId : sample string 2
     * MOrderDetailPrice : 3.0
     * ProductName : sample string 4
     * ProductPrice : 5.0
     * Postage : 6.0
     * ProductNum : 7
     * GoodsInfoId : sample string 8
     * GoodsImgSl : sample string 9
     * CreateTime : sample string 10
     */

    public String MOrderDetailId;
    public String MOrderId;
    public double MOrderDetailPrice;
    public String ProductName;
    public double ProductPrice;
    public double Postage;
    public int ProductNum;
    public String GoodsInfoId;
    public String GoodsImgSl;
    public String CreateTime;


}
