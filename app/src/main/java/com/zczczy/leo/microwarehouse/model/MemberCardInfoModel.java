package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * Created by zczczy on 2016/9/23.
 */

public class MemberCardInfoModel implements Serializable {


         public  String ServiceCardId;
         public  String ServiceName; //服务名称
         public  String GoodsInfoId; //商品ID
         public  String CardImgUrl;  //卡片图片
         public  String MianZhiInfo; //面值信息
         public  String YouHuiInfo;
         public  int AllCount;
         public  int Kbn;

}
