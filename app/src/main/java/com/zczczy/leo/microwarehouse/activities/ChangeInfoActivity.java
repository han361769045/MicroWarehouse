package com.zczczy.leo.microwarehouse.activities;

import android.content.Intent;
import android.widget.EditText;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.MemberInfoModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by Leo on 2016/5/25.
 */
@EActivity(R.layout.activity_change_info)
public class ChangeInfoActivity extends BaseActivity {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    EditText edt_content;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @Extra
    MemberInfoModel memberInfoModel;

    @Extra
    String content;

    @Extra
    String flagT;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
    }

    @AfterViews
    void afterView() {
        edt_content.setHint(content);
        if ("1".equals(flagT)) {
            myTitleBar.setTitle("修改姓名");
        } else if ("2".equals(flagT)) {
            myTitleBar.setTitle("修改QQ");
        } else if ("3".equals(flagT)) {
            myTitleBar.setTitle("修改博客");
        } else if ("4".equals(flagT)) {
            myTitleBar.setTitle("修改邮箱");
        }
    }


    @Click
    void btn_comment() {
        if (AndroidTool.checkIsNull(edt_content)) {
            AndroidTool.showToast(this, "请输入内容！");
        } else {
            if ("1".equals(flagT)) {
                memberInfoModel.MemberRealName = edt_content.getText().toString();
            } else if ("2".equals(flagT)) {
                memberInfoModel.MemberQQ = edt_content.getText().toString();
            } else if ("3".equals(flagT)) {
                memberInfoModel.MemberBlog = edt_content.getText().toString();
            } else if ("4".equals(flagT)) {
                memberInfoModel.MemberEmail = edt_content.getText().toString();
            }
            AndroidTool.showLoadDialog(this);
            changeInfo();
        }
    }


    @Background
    void changeInfo() {
        memberInfoModel.UserPw = null;
        BaseModel bmj = myRestClient.perfectMemberInfo(memberInfoModel);
        aftersetInfo(bmj);
    }

    @UiThread
    void aftersetInfo(BaseModel bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, "网络连接失败");
        } else if (bmj.Successful) {
            AndroidTool.showToast(this, bmj.Error);
            Intent intent = new Intent();
            intent.putExtra("contents", edt_content.getText().toString());
            if ("1".equals(flagT)) {
                setResult(1000, intent);
            } else if ("2".equals(flagT)) {
                setResult(1001, intent);
            } else if ("3".equals(flagT)) {
                setResult(1002, intent);
            } else if ("4".equals(flagT)) {
                setResult(1003, intent);
            }
            finish();
        } else {
            AndroidTool.showToast(this, bmj.Error);
        }
    }


}
