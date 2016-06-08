package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * @author Created by LuLeo on 2016/6/8.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/8.
 */
public class CouponModel implements Serializable {

    public int CouponId;

    public String CouponNo;

    public int CouponType;

    public String CouponPrice;

    public String CreateTime;

    public String UseTime;

    public int AllocationStatus;

    public int UseStatus;

    public String Owner;

    public String Creator;

}
