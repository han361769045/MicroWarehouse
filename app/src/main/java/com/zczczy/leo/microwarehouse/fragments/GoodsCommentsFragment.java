package com.zczczy.leo.microwarehouse.fragments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.GoodsCommentsAdapter;
import com.zczczy.leo.microwarehouse.model.GoodsCommentsModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by LuLeo on 2016/6/16.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/16.
 */
@EFragment(R.layout.activity_goods_comments)
public class GoodsCommentsFragment extends BaseUltimateRecyclerViewFragment<GoodsCommentsModel> implements BaseUltimateRecyclerViewAdapter.DynamicHeight {


    @ViewById
    LinearLayout parent;

    @FragmentArg
    String goodsId;

    HashMap<Integer, Integer> itemHeight = new HashMap<>();

    int sumHeight;

    @Bean
    void setMyAdapter(GoodsCommentsAdapter myAdapter) {
        this.myAdapter = myAdapter;
        myAdapter.setDynamicHeight(this);
    }


    @AfterViews
    void afterView() {
        myTitleBar.setVisibility(View.GONE);
        empty_view.setText(empty_review);
        ultimateRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh, goodsId);
    }

    @Override
    @UiThread
    public void HeightChange(int position, int height) {
        itemHeight.put(position, height);
        sumHeight = SumHashItem(itemHeight);

        float density = getActivity().getResources().getDisplayMetrics().density;
        float viewHeight = sumHeight * density;
        Log.e("======================", viewHeight + "");
        if (position == 0) {
        }
        parent.getLayoutParams().height = (int) viewHeight * myAdapter.getItems().size();
        Log.e("======================", parent.getHeight() + "");
//        parent.getLayoutParams().height = (int) viewHeight;
    }

    int SumHashItem(HashMap<Integer, Integer> hashMap) {
        int sum = 0;

        for (Map.Entry<Integer, Integer> myItem : hashMap.entrySet()) {
            sum += myItem.getValue();
        }

        return sum;
    }
}
