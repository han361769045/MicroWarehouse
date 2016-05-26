package com.zczczy.leo.microwarehouse.activities;

import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
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
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2016/5/22.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById
    EditText editUsername, editPassword, edit_code;

    @ViewById
    LinearLayout ll_code, ll_password, ll_forget;

    @RestService
    MyRestClient myRestClient;

    @ViewById
    RadioButton rb_code;

    @Bean
    MyErrorHandler myErrorHandler;

    @ViewById
    Button btn_send_code;

    @StringRes
    String text_send_message, text_timer;

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
//            edit_code.setEnabled(true);
        } else {
//            edit_code.setEnabled(false);
        }
        readSmsContent = new ReadSmsContent(new Handler(), this, edit_code);
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, readSmsContent);
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
//                edit_code.setEnabled(true);
                sendCode();
            }
        }
    }

    //获取验证码
    @Background
    void sendCode() {
        Map<String, String> map = new HashMap<>();
        map.put("Mobile", editUsername.getText().toString());
        map.put("SendType", Constants.LOING_CODE + "");
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
        BaseModelJson<String> bmj = myRestClient.login(editUsername.getText().toString().trim(),
                rb_code.isChecked() ? edit_code.getText().toString().trim() : editPassword.getText().toString().trim(),
                rb_code.isChecked() ? Constants.MOBILE_LOGIN : Constants.NORMAL_LOGIN, Constants.ANDROID);
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
