package com.zczczy.leo.microwarehouse.model;

/**
 * 银行
 * Created by darkwh on 2015/6/30.
 */
public class UpdateAppModel {

    public int Kbn;   //应用类型(1:android,2:ios_appstore,3:ios_qiye)

    public int VersionCode;  //升级应用版本号

    public String VersionUrl; //升级应用版本地址

    public String Remark;  //备注说明

    public String CreateTime;   //最新更新时间


}
