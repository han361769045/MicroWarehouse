package com.zczczy.leo.microwarehouse.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

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

    MemberInfoModel model;

    boolean isRegister;

    CountDownTimer countDownTimer;

    ReadSmsContent readSmsContent;

    AlertDialog.Builder adb;

    AlertDialog ad;

    View view;

    TextView txt_perfect;

    EditText edt_username, edt_password, edt_confirm_pass, edt_email;

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
                //自动读取短信
//                isRegister = true;
//                readSmsContent = new ReadSmsContent(new Handler(), this, edit_code);
//                this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, readSmsContent);
                editUsername.setText(StringUtils.isEmpty(telephonyManager.getLine1Number()) ? "" : telephonyManager.getLine1Number().substring(3));
                Selection.setSelection(editUsername.getText(), editUsername.length());
            }
        } else {
            //自动读取短信
//            isRegister = true;
//            readSmsContent = new ReadSmsContent(new Handler(), this, edit_code);
//            this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, readSmsContent);
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
        //自动读取短信
//        isRegister = true;
//        readSmsContent = new ReadSmsContent(new Handler(), this, edit_code);
//        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, readSmsContent);
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
            if (!bmj.Data.CheckUserInfo) {
                if (adb == null) {
                    model = bmj.Data;
                    adb = new AlertDialog.Builder(this);
                    view = layoutInflater.inflate(R.layout.perfect_infor_dialog, null);
                    txt_perfect = (TextView) view.findViewById(R.id.txt_perfect);
                    edt_username = (EditText) view.findViewById(R.id.edt_username);
                    edt_password = (EditText) view.findViewById(R.id.edt_password);
                    edt_confirm_pass = (EditText) view.findViewById(R.id.edt_confirm_pass);
                    edt_email = (EditText) view.findViewById(R.id.edt_email);
                    txt_perfect.setText(Html.fromHtml(this.getString(R.string.text_perfect)));
                    view.findViewById(R.id.txt_reset).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            edt_username.setText("");
                            edt_password.setText("");
                            edt_confirm_pass.setText("");
                            edt_email.setText("");
                        }
                    });
                    view.findViewById(R.id.txt_confirm).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (AndroidTool.checkIsNull(edt_username)) {
                                AndroidTool.showToast(LoginActivity.this, "用户名不能为空");
                            } else if (AndroidTool.checkIsNull(edt_password)) {
                                AndroidTool.showToast(LoginActivity.this, "面膜不能为空");
                            } else if (AndroidTool.checkIsNull(edt_confirm_pass)) {
                                AndroidTool.showToast(LoginActivity.this, "确认密码不能为空");
                            } else if (AndroidTool.checkIsNotEqual(edt_password, edt_confirm_pass)) {
                                AndroidTool.showToast(LoginActivity.this, "两次密码不一致");
                            } else if (AndroidTool.checkIsNull(edt_email)) {
                                AndroidTool.showToast(LoginActivity.this, "邮箱不能为空");
                            } else {
                                AndroidTool.showLoadDialog(LoginActivity.this);
                                perfectUserInfo();
                            }
                        }
                    });
                    adb.setView(view);
                    ad = adb.create();
                    ad.setCancelable(false);
                    ad.setCanceledOnTouchOutside(false);
                }
                ad.show();
            } else {
                pre.token().put(bmj.Data.Token);
                pre.nickName().put(editUsername.getText().toString());
                pre.userTypeStr().put(bmj.Data.UserTypeStr);
                pre.avatar().put(bmj.Data.HeadImg);
                pre.userType().put(bmj.Data.UserType);
                pre.jPushAlias().put(bmj.Data.UserInfoId);
                myBackgroundTask.setAlias();
                finish();
            }
        } else {
            AndroidTool.showToast(this, bmj.Error);
        }
    }


    @Background
    void perfectUserInfo() {
        myRestClient.setHeader("Token", model.Token);
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        model.MemberEmail = edt_email.getText().toString();
        model.UserPw = edt_password.getText().toString();
        model.ConfirmUserPw = edt_confirm_pass.getText().toString();
        model.UserLoginStr = edt_username.getText().toString();
        afterPerfectUserInfo(myRestClient.perfectUserInfo(model));
    }

    @UiThread
    void afterPerfectUserInfo(BaseModel result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            AndroidTool.showToast(this, "保存成功");
            pre.token().put(model.Token);
            pre.nickName().put(editUsername.getText().toString());
            pre.userTypeStr().put(model.UserTypeStr);
            pre.avatar().put(model.HeadImg);
            pre.userType().put(model.UserType);
            pre.jPushAlias().put(model.UserInfoId);
            myBackgroundTask.setAlias();
            finish();
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
