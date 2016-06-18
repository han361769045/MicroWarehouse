package com.zczczy.leo.microwarehouse.activities;

import android.widget.Button;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.TaskOrderModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * @author Created by LuLeo on 2016/6/17.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/17.
 */
@EActivity(R.layout.activity_task_order_detail)
public class TaskOrderDetailActivity extends BaseActivity {

    @ViewById
    TextView txt_title, txt_publish_time, txt_content, txt_task_price, txt_publisher, txt_receiver;

    @ViewById
    Button btn_receive;

    @Extra
    String TaskOrderId;

    @Bean
    MyErrorHandler myErrorHandler;

    @RestService
    MyRestClient myRestClient;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
    }

    @AfterViews
    void afterView() {
        AndroidTool.showLoadDialog(this);
        getTaskOrderById();
    }

    @Background
    void getTaskOrderById() {
        afterGetTaskOrderById(myRestClient.getTaskOrderById(TaskOrderId));
    }

    @UiThread
    void afterGetTaskOrderById(BaseModelJson<TaskOrderModel> result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            txt_title.setText(result.Data.TaskTitle);
            txt_publish_time.setText(result.Data.PublishTime);
            txt_content.setText(result.Data.TaskOrderContent);
            txt_task_price.setText(result.Data.Expense);
//            txt_publisher.setText(result.Data.);
        }
    }

}
