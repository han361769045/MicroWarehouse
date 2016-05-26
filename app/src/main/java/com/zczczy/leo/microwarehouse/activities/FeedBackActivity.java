package com.zczczy.leo.microwarehouse.activities;

import android.widget.EditText;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModel;
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
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2016/5/26.
 */
@EActivity(R.layout.activity_feedback)
public class FeedbackActivity extends BaseActivity {

    @ViewById
    EditText edt_content;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @StringRes
    String text_feedback_tip;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
    }

    @Click
    void btn_comment() {
        if (AndroidTool.checkIsNull(edt_content)) {
            AndroidTool.showToast(this, text_feedback_tip);
        } else {
            AndroidTool.showLoadDialog(this);
            feedback();
        }
    }

    @Background
    void feedback() {
        Map<String, String> map = new HashMap<>();
        map.put("FeedBackContent", edt_content.getText().toString().trim());
        afterFeedback(myRestClient.insertFeedback(map));
    }

    @UiThread
    void afterFeedback(BaseModel result) {
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
