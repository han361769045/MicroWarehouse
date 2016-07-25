package com.zczczy.leo.microwarehouse.fragments;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.TaskOrderDetailActivity_;
import com.zczczy.leo.microwarehouse.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.TaskOrderAdapter;
import com.zczczy.leo.microwarehouse.model.BaseModel;
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
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * @author Created by LuLeo on 2016/6/17.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/17.
 */
@EFragment(R.layout.activity_ultimate_recycler_view)
public class MemberTaskOrderFragment extends BaseUltimateRecyclerViewFragment<TaskOrderModel> {


    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @FragmentArg
    String type;

    @Bean
    void setMyAdapter(TaskOrderAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        myTitleBar.setVisibility(View.GONE);
        if ("0".equals(type)) {
            bus.register(this);
        }
        myAdapter.setOnItemClickListener(new BaseUltimateRecyclerViewAdapter.OnItemClickListener<TaskOrderModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, TaskOrderModel obj, int position) {
                TaskOrderDetailActivity_.intent(MemberTaskOrderFragment.this).TaskOrderId(String.valueOf(obj.TaskOrderId)).start();
            }

            @Override
            public void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });
        myAdapter.setOnItemLongClickListener(new BaseUltimateRecyclerViewAdapter.OnItemLongClickListener<TaskOrderModel>() {

            @Override
            public void onItemLongClick(final RecyclerView.ViewHolder viewHolder, final TaskOrderModel obj, int position) {
                if ("0".equals(type) && (obj.TaskStatus == 0 || obj.TaskStatus == 1 || obj.TaskStatus == 4)) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setTitle("提示").setMessage("确认删除任务吗？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AndroidTool.showLoadDialog(getActivity());
                            delTaskOrderById(obj.TaskOrderId, viewHolder.getAdapterPosition());
                        }
                    }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
                }
            }

            @Override
            public void onHeaderLongClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });
    }

    @Background
    void delTaskOrderById(int id, int position) {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterDelTaskOrderById(myRestClient.delTaskOrderById(id), position);
    }

    @UiThread
    void afterDelTaskOrderById(BaseModel result, int position) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!result.Successful) {
            AndroidTool.showToast(this, result.Error);
        } else {
            AndroidTool.showToast(this, "删除成功");
            myAdapter.notifyItemRemoved(position);
        }
    }


    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh, "1", type);
    }
}
