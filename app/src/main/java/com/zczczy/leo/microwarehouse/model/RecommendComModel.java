package com.zczczy.leo.microwarehouse.model;

/**
 * Created by zhangyan on 2016/10/12.
 * 推荐商品
 */
public class RecommendComModel {

    /**
     * GoodsInfoId : sample string 1
     * GodosName : sample string 2
     * GoodsImgSl : sample string 3
     * GoodsXl : 4
     * GoodsPrice : 5.0
     * GoodsBatPrice : 6.0
     * GoodsStatus : 7
     * GoodsStock : 8
     */

    // 商品id
    public String GoodsInfoId;
    // 商品名称
    public String GodosName;
    // 商品图片
    public String GoodsImgSl;
    // 商品销量
    public int GoodsXl;
    // 商品价格
    public double GoodsPrice;
    // 商品经销商价格
    public double GoodsBatPrice;
    // 商品状态
    public int GoodsStatus;
    // 商品库存
    public int GoodsStock;

}
