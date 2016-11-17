package com.zczczy.leo.microwarehouse.activities;

import android.widget.EditText;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.DealerApplyModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by Leo on 2016/5/26.
 * 申请经销商
 */
@EActivity(R.layout.activity_apply_dealer)
public class ApplyDealerActivity extends BaseActivity {

    @ViewById
    EditText edt_real_name, edit_phone;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
    }

    @Click
    void btn_apply() {
        if (AndroidTool.checkIsNull(edt_real_name)) {
            AndroidTool.showToast(this, "真实姓名不能为空");
        } else if (AndroidTool.checkIsNull(edit_phone)) {
            AndroidTool.showToast(this, "手机号不能为空");
        } else {
            AndroidTool.showLoadDialog(this);
            applyDealer();
        }
    }

    @Background
    void applyDealer() {
        DealerApplyModel dealerApplyModel = new DealerApplyModel();
        dealerApplyModel.RealName = edt_real_name.getText().toString().trim();
        dealerApplyModel.Mobile = edit_phone.getText().toString().trim();
        afterApplyDealer(myRestClient.dealerApply(dealerApplyModel));
    }

    @UiThread
    void afterApplyDealer(BaseModel result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            AndroidTool.showToast(this, result.Error);
            finish();
        }
    }

}
