package com.zczczy.leo.microwarehouse.activities;

import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.LoginInfo;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by zczczy on 2016/5/21.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById
    Button login_btn;
    @ViewById
    EditText username,psd;
    @ViewById
    Button regist;

    @RestService
    MyRestClient myRestClient;
    @Bean
    MyErrorHandler myErrorHandler;

    @Pref
    MyPrefs_ pre;

    @ViewById
    RadioButton rb_mobile,rb_user;
    @ViewById
    LinearLayout mobile_login,username_login;


    @AfterViews
    void afterView(){

    }


    @Click
    void login_btn(){
        if (AndroidTool.checkIsNull(username)) {
            AndroidTool.showToast(this, "帐号不能为空");
        } else if (AndroidTool.checkIsNull(psd)) {
            AndroidTool.showToast(this, "密码不能为空");
        } else {
            AndroidTool.showLoadDialog(this);
            login();
        }
    }

    @Background
    void login(){
        BaseModelJson<LoginInfo> bmj = myRestClient.login(username.getText().toString().trim(),
                psd.getText().toString().trim(), "3", MyApplication.ANDROID);
        afterLogin(bmj);
    }

    @UiThread
    void afterLogin(BaseModelJson<LoginInfo> bmj){
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (bmj.Successful) {
            pre.token().put(bmj.Data.Token);
            pre.userType().put(bmj.Data.LoginType);
            finish();
        } else {
            AndroidTool.showToast(this, bmj.Error);
        }
    }

    @CheckedChange
    void rb_mobile(boolean checked){
        if(checked){
            mobile_login.setVisibility(View.VISIBLE);
            regist.setVisibility(View.GONE);
        }
        else {
            mobile_login.setVisibility(View.GONE);
            regist.setVisibility(View.VISIBLE);
        }

    }
    @CheckedChange
    void rb_user(boolean checked){
        if(checked){
            username_login.setVisibility(View.VISIBLE);
            regist.setVisibility(View.VISIBLE);
        }
        else {
            username_login.setVisibility(View.GONE);
            regist.setVisibility(View.GONE);
        }

    }


}
