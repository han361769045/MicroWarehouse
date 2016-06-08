package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * @author Created by LuLeo on 2016/6/8.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/8.
 */
public class TaskOrderModel implements Serializable {

    public int TaskOrderId;

    public String TaskOrderContent;//内容

    public String Expense;//费用

    public String AreaRange;//跑腿范围

    public String TaskTime; //跑腿时间

    public String WeightRange;//重量

    public String Publisher;//发布者

    public String Receiver;//接受者

    public int Status;//任务状态

    public String PublishTime;//发布时间

    public String ReveiveTime;//接受时间

    public String FinishTime;//完成时间

    public String Cancel;//取消时间

    public String expiredTime;//过期时间

}
