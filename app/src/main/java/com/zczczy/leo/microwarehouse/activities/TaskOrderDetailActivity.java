package com.zczczy.leo.microwarehouse.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModel;
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
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.HashMap;
import java.util.Map;

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
    RelativeLayout rl_publisher, rl_receiver;

    @ViewById
    Button btn_receive, btn_cancel, btn_finish;

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
            txt_publisher.setText(result.Data.PublishLogin);
            txt_receiver.setText(result.Data.ReveiveLogin);
            if (result.Data.TaskStatus == 0) {
                btn_receive.setVisibility(View.VISIBLE);
            } else if (result.Data.TaskStatus == 1 && pre.nickName().get().equals(result.Data.ReveiveLogin)) {
                btn_cancel.setVisibility(View.VISIBLE);
            } else if (result.Data.TaskStatus == 2 && pre.nickName().get().equals(result.Data.PublishLogin)) {
                btn_finish.setVisibility(View.VISIBLE);
            }
        }
    }

    @Click
    void btn_receive() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("提示").setMessage("确定要完成？").setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AndroidTool.showLoadDialog(TaskOrderDetailActivity.this);
                lootTaskOrder();
            }
        }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
    }

    @Background
    void lootTaskOrder() {
        Map<String, String> map = new HashMap<>(1);
        map.put("TaskOrderId", TaskOrderId);
        afterLootTaskOrder(myRestClient.lootTaskOrder(map));
    }

    @UiThread
    void afterLootTaskOrder(BaseModel result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            btn_receive.setVisibility(View.GONE);
        }
    }

    @Click
    void btn_cancel() {

    }

    @Click
    void btn_finish() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("提示").setMessage("确定要完成？").setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AndroidTool.showLoadDialog(TaskOrderDetailActivity.this);
                finishTaskOrder();
            }
        }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
    }

    @Background
    void finishTaskOrder() {
        Map<String, String> map = new HashMap<>(1);
        map.put("TaskOrderId", TaskOrderId);
        afterFinishTaskOrder(myRestClient.finishTaskOrder(map));
    }

    @UiThread
    void afterFinishTaskOrder(BaseModel result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            btn_finish.setVisibility(View.GONE);
        }
    }

}
