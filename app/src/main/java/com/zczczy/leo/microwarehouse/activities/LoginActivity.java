package com.zczczy.leo.microwarehouse.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.telephony.TelephonyManager;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.listener.ReadSmsContent;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.MemberInfoModel;
import com.zczczy.leo.microwarehouse.rest.MyBackgroundTask;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2016/5/22.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById
    EditText editPassword, editUsername, edit_code;

    @ViewById
    LinearLayout ll_code, ll_username, ll_password, ll_forget;

    @RestService
    MyRestClient myRestClient;

    @ViewById
    RadioButton rb_code;

    @Bean
    MyErrorHandler myErrorHandler;

    @Bean
    MyBackgroundTask myBackgroundTask;

    @ViewById
    Button btn_send_code;

    @StringRes
    String text_send_message, text_timer;

    @SystemService
    TelephonyManager telephonyManager;

    boolean isRegister;

    CountDownTimer countDownTimer;

    ReadSmsContent readSmsContent;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        getCountDownTimer();
        if (AndroidTool.getCodeTime(pre.loginTimerCode().get()) < 120000L) {
            countDownTimer.start();
        }
        getPermissions();
    }

    @TextChange
    void editUsername() {

    }


    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();

            if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_SMS);
            }
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 127);
            } else {
                isRegister = true;
                readSmsContent = new ReadSmsContent(new Handler(), this, edit_code);
                this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, readSmsContent);
                editUsername.setText(StringUtils.isEmpty(telephonyManager.getLine1Number()) ? "" : telephonyManager.getLine1Number().substring(3));
                Selection.setSelection(editUsername.getText(), editUsername.length());
            }
        } else {
            isRegister = true;
            readSmsContent = new ReadSmsContent(new Handler(), this, edit_code);
            this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, readSmsContent);
            editUsername.setText(StringUtils.isEmpty(telephonyManager.getLine1Number()) ? "" : telephonyManager.getLine1Number().substring(3));
            Selection.setSelection(editUsername.getText(), editUsername.length());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        isRegister = true;
        readSmsContent = new ReadSmsContent(new Handler(), this, edit_code);
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, readSmsContent);
        editUsername.setText(StringUtils.isEmpty(telephonyManager.getLine1Number()) ? "" : telephonyManager.getLine1Number().substring(3));
        Selection.setSelection(editUsername.getText(), editUsername.length());

    }


    @Click
    void login_btn() {
        if (AndroidTool.checkIsNull(editUsername)) {
            AndroidTool.showToast(this, "手机号不能为空");
        } else if (rb_code.isChecked() && AndroidTool.checkIsNull(edit_code)) {
            AndroidTool.showToast(this, "验证码不能为空");
        } else if (!rb_code.isChecked() && AndroidTool.checkIsNull(editPassword)) {
            AndroidTool.showToast(this, "密码不能为空");
        } else {
            AndroidTool.showLoadDialog(this);
            login();
        }
    }


    void getCountDownTimer() {
        countDownTimer = new CountDownTimer(AndroidTool.getCodeTime(pre.loginTimerCode().get()), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                pre.loginTimerCode().put(System.currentTimeMillis() + millisUntilFinished);
                btn_send_code.setEnabled(false);
                btn_send_code.setText(String.format(text_timer, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                pre.loginTimerCode().put(0L);
                btn_send_code.setEnabled(true);
                btn_send_code.setText(text_send_message);
            }
        };
    }

    @Click
    void btn_send_code() {
        if (AndroidTool.checkIsNull(editUsername)) {
            AndroidTool.showToast(this, "手机号不能为空");
        } else {
            if (pre.loginTimerCode().get() == 0L || AndroidTool.getCodeTime(pre.loginTimerCode().get()) >= 120000L) {
                getCountDownTimer();
                countDownTimer.start();
                sendCode();
            }
        }
    }

    //获取验证码
    @Background
    void sendCode() {
        Map<String, String> map = new HashMap<>();
        map.put("Mobile", editUsername.getText().toString());
        map.put("SendType", Constants.LOGIN_CODE + "");
        afterSendCode(myRestClient.sendMsg(map));
    }

    @UiThread
    void afterSendCode(BaseModel bmj) {
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (bmj.Successful) {
            AndroidTool.showToast(this, bmj.Error);
        } else {
            AndroidTool.showToast(this, bmj.Error);
        }
    }

    @CheckedChange
    void rb_code(boolean isChecked) {
        if (isChecked) {
            ll_code.setVisibility(View.VISIBLE);
            ll_password.setVisibility(View.GONE);
            ll_forget.setVisibility(View.GONE);
        } else {
            ll_code.setVisibility(View.GONE);
            ll_password.setVisibility(View.VISIBLE);
            ll_forget.setVisibility(View.VISIBLE);
        }
    }

    @EditorAction
    void editPassword(int actionId) {
        if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
            login_btn();
        }
    }

    @EditorAction
    void edit_code(int actionId) {
        if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
            login_btn();
        }
    }


    @Background
    void login() {
        BaseModelJson<MemberInfoModel> bmj = myRestClient.login(editUsername.getText().toString().trim(),
                rb_code.isChecked() ? edit_code.getText().toString().trim() : editPassword.getText().toString().trim(),
                rb_code.isChecked() ? Constants.MOBILE_LOGIN : Constants.NORMAL_LOGIN, Constants.ANDROID);
        afterLogin(bmj);
    }

    @UiThread
    void afterLogin(BaseModelJson<MemberInfoModel> bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (bmj.Successful) {
            pre.token().put(bmj.Data.Token);
            pre.nickName().put(editUsername.getText().toString());
            pre.userTypeStr().put(bmj.Data.UserTypeStr);
            pre.avatar().put(bmj.Data.HeadImg);
            pre.userType().put(bmj.Data.UserType);
            pre.jPushAlias().put(bmj.Data.UserInfoId);
            myBackgroundTask.setAlias();
            finish();
        } else {

            AndroidTool.showToast(this, bmj.Error);
        }
    }


    @Override
    public void finish() {
        if (isRegister) {
            //注销内容监听者
            this.getContentResolver().unregisterContentObserver(readSmsContent);
        }
        super.finish();
    }

    @Click
    void forget() {

    }

}
