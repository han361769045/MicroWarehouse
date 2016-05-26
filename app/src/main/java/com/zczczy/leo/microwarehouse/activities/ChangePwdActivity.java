package com.zczczy.leo.microwarehouse.activities;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.MemberInfoModel;
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
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2016/5/25.
 */
@EActivity(R.layout.activity_change_password)
public class ChangePwdActivity extends BaseActivity {

    @ViewById
    EditText gar_old, gar_password, gar_password_confirm;

    @ViewById
    LinearLayout ll_gar_old;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @Extra
    MemberInfoModel memberInfoModel;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
    }

    @AfterViews
    void afterView() {
        ll_gar_old.setVisibility(StringUtils.isEmpty(memberInfoModel.UserPw) ? View.GONE : View.VISIBLE);
    }

    @Click
    void btn_change() {
        if (ll_gar_old.isShown() && AndroidTool.checkIsNull(gar_old)) {
            AndroidTool.showToast(this, "原始密码不能为空");
        } else if (AndroidTool.checkIsNull(gar_password)) {
            AndroidTool.showToast(this, "新始密码不能为空");
        } else if (AndroidTool.checkIsNull(gar_password_confirm)) {
            AndroidTool.showToast(this, "确认密码不能为空");
        } else if (!gar_password.getText().toString().equals(gar_password_confirm.getText().toString())) {
            AndroidTool.showToast(this, "两次密码输入不一致");
        } else {
            AndroidTool.showLoadDialog(this);
            if (ll_gar_old.isShown()) {
                changePassword();
            } else {
                changeInfo();
            }
        }
    }

    @Background
    void changePassword() {
        Map<String, String> map = new HashMap<>();
        map.put("OldPw", gar_old.getText().toString());
        map.put("NewPw", gar_password.getText().toString());
        map.put("ConfirmPw", gar_password_confirm.getText().toString());
        afterChangePassword(myRestClient.updPassWord(map));
    }

    @UiThread
    void afterChangePassword(BaseModel bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (bmj.Successful) {
            AndroidTool.showToast(this, "修改成功");
            setResult(1000);
            finish();
        } else {
            AndroidTool.showToast(this, bmj.Error);
        }
    }

    @Background
    void changeInfo() {
        memberInfoModel.UserPw = gar_password.getText().toString().trim();
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
            setResult(1001);
            finish();
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
