package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * @author Created by LuLeo on 2016/6/15.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/15.
 */
public class DepotModel implements Serializable {

    /**
     * UserInfoId : sample string 1
     * DepotId : sample string 2
     * AreaId : 3
     * DepotName : sample string 4
     * Coordinates : sample string 5
     * DepotStatus : 6
     * Address : sample string 7
     * DepotImgUrl : sample string 8
     * DepotType : sample string 9
     */

    public String UserInfoId; //用户信息主键ID
    public String DepotId; //仓库id
    public int AreaId;  //区ID
    public String DepotName; //仓库名称
    public String Coordinates; //仓库坐标
    public int DepotStatus;   //仓库状态(0:禁用，1：启用)
    public String Address;  //微仓地址
    public String DepotImgUrl; //仓库图片
    public String DepotType; //仓库类型（00:固定仓库,01:移动仓库）

}
