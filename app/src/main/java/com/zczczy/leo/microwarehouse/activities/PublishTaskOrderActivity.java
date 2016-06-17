package com.zczczy.leo.microwarehouse.activities;

import android.view.View;
import android.widget.EditText;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.TaskOrderModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * @author Created by LuLeo on 2016/6/17.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/17.
 */
@EActivity(R.layout.activity_publish_task_order)
public class PublishTaskOrderActivity extends BaseActivity {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    EditText edt_title, edt_content, edt_task_price;

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

    @AfterViews
    void afterView() {
        myTitleBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AndroidTool.checkIsNull(edt_title)) {
                    AndroidTool.showToast(PublishTaskOrderActivity.this, "标题不能为空");
                } else if (AndroidTool.checkIsNull(edt_content)) {
                    AndroidTool.showToast(PublishTaskOrderActivity.this, "内容不能为空");
                } else if (AndroidTool.checkIsNull(edt_task_price)) {
                    AndroidTool.showToast(PublishTaskOrderActivity.this, "钱不能为空");
                } else {
                    publisherTaskOrder();
                }
            }
        });
    }

    @Background
    void publisherTaskOrder() {
        TaskOrderModel model = new TaskOrderModel();
        model.TaskTitle = edt_title.getText().toString().trim();
        model.TaskOrderContent = edt_content.getText().toString().trim();
        model.Expense = edt_task_price.getText().toString().trim();
        afterPublisherTaskOrder(myRestClient.publisherTaskOrder(model));
    }

    @UiThread
    void afterPublisherTaskOrder(BaseModel result) {
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }
}
