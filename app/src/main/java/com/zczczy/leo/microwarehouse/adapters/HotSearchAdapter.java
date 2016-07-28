package com.zczczy.leo.microwarehouse.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.microwarehouse.activities.SearchActivity;
import com.zczczy.leo.microwarehouse.items.HotSearchItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * @author Created by LuLeo on 2016/7/28.
 *         you can contact me at :361769045@qq.com
 * @since 2016/7/28.
 */
@EBean
public class HotSearchAdapter extends BaseRecyclerViewAdapter<String> {


    @RootContext
    SearchActivity searchActivity;

    @Override
    public void getMoreData(Object... objects) {
        afterGetMoreData(myRestClient.getKeyWordList());
    }

    @UiThread
    protected void afterGetMoreData(BaseModelJson<List<String>> result) {
        AndroidTool.dismissLoadDialog();
        if (result == null) {
            result = new BaseModelJson<>();
//            AndroidTool.showToast(context, no_net);
        } else if (result.Successful) {
            clear();
            if (result.Data.size() > 0) {
                searchActivity.ll_hot.setVisibility(View.VISIBLE);
                insertAll(result.Data, getItemCount());
            } else {
                searchActivity.ll_hot.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return HotSearchItemView_.build(context);
    }
}
