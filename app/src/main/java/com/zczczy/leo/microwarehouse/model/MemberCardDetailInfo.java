package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zczczy on 2016/9/26.
 */

public class MemberCardDetailInfo implements Serializable {


    public  String ServiceCardId;
    public  String MyCardInfoId;
    public  String UserInfoId;
    public  String CardNo; //卡片ID
    public  String BuyDateTime;
    public  String UseCount;
    public  String MianZhiInfo; //面值信息
    public  String YouHuiInfo;
    public  String CardImgUrl;  //卡片图片
    public  int AllCount;
    public String OrderDetailId;
    public  String IsUseOver;
    public  String GoodsInfoId;

    public List<MemberCardCommentsModel> CardDetailInfoList; //会员卡列表信息
}
