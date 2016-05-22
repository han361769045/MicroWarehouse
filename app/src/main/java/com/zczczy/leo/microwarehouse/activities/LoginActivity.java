package com.zczczy.leo.microwarehouse.activities;

import android.net.Uri;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.listener.ReadSmsContent;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by Leo on 2016/5/22.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById
    EditText editUsername, editPassword;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    ReadSmsContent readSmsContent;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        readSmsContent = new ReadSmsContent(new Handler(), this, editUsername);
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, readSmsContent);
    }

    @Click
    void login_btn() {
        if (AndroidTool.checkIsNull(editUsername)) {
            AndroidTool.showToast(this, "帐号不能为空");
        } else if (AndroidTool.checkIsNull(editPassword)) {
            AndroidTool.showToast(this, "密码不能为空");
        } else {
            AndroidTool.showLoadDialog(this);
            login();
        }
    }


    @EditorAction
    void editPassword(int actionId) {
        if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
            login_btn();
        }
    }


    @Background
    void login() {
        BaseModelJson<String> bmj = myRestClient.login(editUsername.getText().toString().trim(),
                editPassword.getText().toString().trim(), Constants.NORMAL_LOGIN, Constants.ANDROID);
        afterLogin(bmj);
    }

    @UiThread
    void afterLogin(BaseModelJson<String> bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (bmj.Successful) {
            pre.token().put(bmj.Data);
            pre.nickName().put(editUsername.getText().toString());
            finish();
        } else {
            AndroidTool.showToast(this, bmj.Error);
        }
    }

    @Override
    public void finish() {
        //注销内容监听者
        this.getContentResolver().unregisterContentObserver(readSmsContent);
        super.finish();
    }

    @Click
    void forget() {

    }

}
