package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * Created by Leo on 2016/5/25.
 */
public class NoticeInfoModel implements Serializable {


    /**
     * NoticeInfoId : 1
     * NoticeInfoTitle : sample string 2
     * NoticeStatus : 3
     */

    public int NoticeInfoId; //公告主键ID
    public String NoticeInfoTitle; //公告标题
    public int NoticeStatus; //公告状态（0：禁用，1：启用）

}
