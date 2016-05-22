package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * Created by Leo on 2016/5/20.
 */
public class CartModel implements Serializable {

    public String cartId;

    public boolean isChecked;

    public String goodsName;

    public String goodsImage;

    public double goodsPrice;

    public int goodsNum;

}
