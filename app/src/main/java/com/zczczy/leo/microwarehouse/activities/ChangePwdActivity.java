package com.zczczy.leo.microwarehouse.activities;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.zczczy.leo.microwarehouse.MyApplication;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zczczy on 2016/5/25.
 */
@EActivity(R.layout.activity_change_password)
public class ChangePwdActivity extends  BaseActivity {

    @ViewById
    EditText gar_old, gar_password, gar_password_confirm;

    @RestService
    MyRestClient myRestClient;

    @Pref
    MyPrefs_ pre;

    @Bean
    MyErrorHandler myErrorHandler;

    @AfterViews
    void afterView() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @Click
    void btn_change() {
        if (AndroidTool.checkIsNull(gar_old)) {
            AndroidTool.showToast(this, "原始密码不能为空");
        } else if (AndroidTool.checkIsNull(gar_password)) {
            AndroidTool.showToast(this, "新始密码不能为空");
        } else if (AndroidTool.checkIsNull(gar_password_confirm)) {
            AndroidTool.showToast(this, "确认密码不能为空");
        } else if (!gar_password.getText().toString().equals(gar_password_confirm.getText().toString())) {
            AndroidTool.showToast(this, "两次密码输入不一致");
        } else {
            AndroidTool.showLoadDialog(this);
            changePassword();
        }
    }

    @Background
    void changePassword() {
        Map<String, String> map = new HashMap<>();
        myRestClient.setHeader("Token", "D463CF459CE7AF242A727787E2DCDC8EC555869244E957647E08EDB14C9597C28CE9FA19437D1EA2");
        myRestClient.setHeader("Kbn", MyApplication.ANDROID);
        map.put("OldPw", gar_old.getText().toString());
        map.put("NewPw", gar_password.getText().toString());
        map.put("ConfirmPw", gar_password_confirm.getText().toString());
        afterChangePassword(myRestClient.UpdPassWord(map));
    }

    @UiThread
    void afterChangePassword(BaseModel bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (bmj.Successful) {
            AndroidTool.showToast(this, "修改成功");
            pre.clear();
            finish();
            LoginActivity_.intent(this).start();
        } else {
            AndroidTool.showToast(this, bmj.Error);
        }
    }

    @EditorAction
    void gar_password_confirm(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            btn_change();
        }
    }
}



