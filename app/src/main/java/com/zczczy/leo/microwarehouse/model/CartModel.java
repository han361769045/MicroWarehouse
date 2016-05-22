package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * Created by Leo on 2016/5/20.
 */
public class CartModel implements Serializable {

    /**
     * BuyCartInfoId : 1
     * GoodsInfoId : sample string 2
     * UserInfoId : sample string 3
     * CreateTime : sample string 4
     * ProductCount : 5
     * GoodsImgSl : sample string 6
     * GoodsPrice : 7.0
     * GodosName : sample string 8
     * GoodsStock : 9
     */

    public int BuyCartInfoId; //购物车主键ID
    public String GoodsInfoId; //商品ID
    public String UserInfoId; //用户主键ID
    public String CreateTime; //加入时间
    public int ProductCount; //购买数量
    public String GoodsImgSl; //产品缩略图
    public double GoodsPrice; //产品价格
    public String GodosName;  //产品名称
    public int GoodsStock;  //产品库存
    public boolean isChecked;
}
