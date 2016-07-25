package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * Created by Leo on 2016/5/22.
 */
public class MemberInfoModel implements Serializable {
    /**
     * MInfoId : sample string 1
     * UserInfoId : sample string 2
     * UserPw : sample string 3
     * MemberEmail : sample string 4
     * MemberQQ : sample string 5
     * MemberBlog : sample string 6
     * HeadImg : sample string 7
     * MemberRealName : sample string 8
     */
    public String MInfoId;
    public String UserInfoId;
    public String UserPw;
    public String ConfirmUserPw;
    public String MemberEmail;
    public String MemberQQ;
    public String MemberBlog;
    public String HeadImg;
    public String MemberRealName;
    public String UserTypeStr; //用户类型描述
    public String UserType;//用户类型
    public String Token;//token
    public String Mobile;
    public String UserLoginStr;
    public boolean CheckUserInfo;
}
