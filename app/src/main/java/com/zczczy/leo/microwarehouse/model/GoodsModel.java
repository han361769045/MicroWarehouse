package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Leo on 2016/5/21.
 */
public class GoodsModel implements Serializable{

    /**
     * GoodsInfoId : sample string 1
     * GodosName : sample string 2
     * GoodsImgSl : sample string 3
     * GoodsXl : 4
     * GoodsPrice : 5.0
     * GoodsBatPrice : 6.0
     */

    public String GoodsInfoId; //商品主键ID
    public String GodosName;  //商品名称
    public String GoodsPrice;  //商品零售价格
    public String GoodsBatPrice; //商品批发价格

    public int GoodsTypeId;  //商品分类主键
    public String StaticHtmlUrl;  //静态页地址
    public String GoodsCreateTime; //商品创建时间
    public int GoodsStatus; //商品状态
    public int GoodsStock;   //商品库存
    public String GoodsImgSl; //商品缩略图
    public int GoodsIsBy;  //商品是否包邮(0:不包邮，1：包邮)
    public int GoodsXl;  //商品销量
    public int ISCommend; //是否推荐（0不推荐，1推荐）
    public String Postage; //邮费
    public int PostageType;  //邮费类型（0包邮，1不累计，2多件累计）
    public String PjNum; //评价数量
    public String PJBfb; //评价百分比
    public  String IsAppointmentPro;
    public int IsUsing;


    public List<GoodsCommentsModel> GoodsCommentsList;  //商品评价

    public List<GoodsImgModel> GoodsImgList;  //商品图片集合

    public List<GoodsAttribute> GoodsAttributeList;//

    public List<GoodsModel> RecommendedGoodsList;//推荐商品

}
