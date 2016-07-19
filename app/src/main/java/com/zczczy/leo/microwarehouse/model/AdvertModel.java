package com.zczczy.leo.microwarehouse.model;


import java.io.Serializable;

/**
 * Created by Leo on 2016/5/22.
 */
public class AdvertModel implements Serializable {


    /**
     * AdvertId : sample string 1
     * AdsenseTypeId : 2
     * AdvertImg : sample string 3
     * InfoId : sample string 4
     * JumpType : 5
     * AdvertStatus : 6
     * CreateTime : sample string 7
     */

    public String AdvertId;
    public int AdsenseTypeId;
    public String AdvertImg;
    public String InfoId;
    public int JumpType;
    public int AdvertStatus;
    public String CreateTime;
    public String AdvertName;
}