package com.zczczy.leo.microwarehouse.activities;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.CustomUltimateRecyclerview;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseVipCardAdapter;

import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by zczczy on 2016/9/23.
 */
@EActivity(R.layout.activity_ultimate_recycler_view)
public abstract class BaseVipCardActivity<T> extends BaseActivity {

    @ViewById
    CustomUltimateRecyclerview ultimateRecyclerView;

    BaseVipCardAdapter<T> myAdapter;

    @ViewById
    TextView empty_view;

    @Bean
    OttoBus bus;

    @ViewById
    MyTitleBar myTitleBar;

    LinearLayoutManager linearLayoutManager;

    GridLayoutManager gridLayoutManager;

    int pageIndex = 1;

    MaterialHeader materialHeader;

    Paint paint = new Paint();

    boolean isRefresh;

    @AfterViews
    void afterRecyclerView() {
        AndroidTool.showLoadDialog(this);
        bus.register(this);
        ultimateRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        verticalItem();
        afterLoadMore();
        ultimateRecyclerView.enableLoadmore();
       // myAdapter.setCustomLoadMoreView(R.layout.custom_bottom_progressbar);
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                if (myAdapter.getItems().size() >= myAdapter.getTotal()) {
//                    AndroidTool.showToast(BaseUltimateRecyclerViewActivity.this, "没有更多的数据了！~");
                    ultimateRecyclerView.disableLoadmore();
                    myAdapter.notifyItemRemoved(itemsCount > 0 ? itemsCount - 1 : 0);
                } else {
                    pageIndex++;
                    afterLoadMore();
                }
            }
        });
        ultimateRecyclerView.setCustomSwipeToRefresh();
        paint.setStrokeWidth(1);
        paint.setColor(Color.WHITE);
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).margin(21).paint(paint).build());
       // refreshingMaterial();
    }

    //线性布局
    void verticalItem() {
        myAdapter.verticalAndHorizontal = BaseVipCardAdapter.VerticalAndHorizontal.Vertical;
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.setAdapter(myAdapter);
    }

    //网格布局
    void horizontalItem() {
        myAdapter.verticalAndHorizontal = BaseVipCardAdapter.VerticalAndHorizontal.Horizontal;
        ultimateRecyclerView.setLayoutManager(gridLayoutManager);
        ultimateRecyclerView.setAdapter(myAdapter);
    }

    abstract void afterLoadMore();


    void refreshingMaterial() {
        materialHeader = new MaterialHeader(this);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        materialHeader.setColorSchemeColors(colors);
        materialHeader.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        materialHeader.setPadding(0, 15, 0, 10);
        materialHeader.setPtrFrameLayout(ultimateRecyclerView.mPtrFrameLayout);
        ultimateRecyclerView.mPtrFrameLayout.autoRefresh(false);
        ultimateRecyclerView.mPtrFrameLayout.setHeaderView(materialHeader);
        ultimateRecyclerView.mPtrFrameLayout.addPtrUIHandler(materialHeader);
        ultimateRecyclerView.mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh = true;
                pageIndex = 1;
                afterLoadMore();
            }
        });
    }

    @Subscribe
    public void notifyUI(BaseModel bm) {
        if (isRefresh) {
            linearLayoutManager.scrollToPosition(0);
            ultimateRecyclerView.mPtrFrameLayout.refreshComplete();
            isRefresh = false;
            if (myAdapter.getItems().size() < myAdapter.getTotal()) {
                ultimateRecyclerView.reenableLoadmore(layoutInflater.inflate(R.layout.custom_bottom_progressbar, null));
            } else {
                ultimateRecyclerView.disableLoadmore();
            }
        } else if (pageIndex == 1) {
            linearLayoutManager.scrollToPosition(0);
        }
    }


    @Override
    public void finish() {
        bus.unregister(this);
        super.finish();
    }
}
