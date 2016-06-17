package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * @author Created by LuLeo on 2016/6/8.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/8.
 */
public class TaskOrderModel implements Serializable {

    public int TaskOrderId;

    public String TaskTitle; //任务标题

    public String TaskOrderContent;//任务详细说明

    public String Expense;//费用

    public String AreaRange;//跑腿范围

    public String Publisher;//发布者

    public String Receiver;//接受者

    public int TaskStatus;//任务状态

    public String PublishTime;//发布时间

    public String ReveiveTime;//接受时间

    public String FinishTime;//完成时间

    public String Cancel;//取消时间

    public String expiredTime;//过期时间

    public String PublishLogin;//发布者

    public String ReveiveLogin;//接受者

    public String PublishHeadImg;//发布者头像地址

    public String ReveiveHeadImg;//接受者头像地址

}
