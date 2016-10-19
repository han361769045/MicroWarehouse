package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.leo.lu.llrecyclerview.LLRecyclerView;
import com.leo.lu.llrecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.leo.lu.llrecyclerview.ui.divideritemdecoration.VerticalDividerItemDecoration;
import com.leo.lu.llrecyclerview.ui.header.RentalsSunHeaderView;
import com.leo.lu.mytitlebar.MyTitleBar;
import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModel;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by Leo on 2016/5/21.
 */
@EActivity(R.layout.activity_ultimate_recycler_view)
public abstract class BaseUltimateRecyclerViewActivity<T> extends BaseActivity {

    @ViewById(resName = "my_title_bar")
    MyTitleBar myTitleBar;

    @ViewById(resName = "ultimate_recycler_view")
    LLRecyclerView ultimateRecyclerView;

    BaseUltimateRecyclerViewAdapter<T> myAdapter;

    @ViewById(resName = "empty_view")
    TextView empty_view;

    @Bean
    OttoBus bus;

    int layoutId;

    LinearLayoutManager linearLayoutManager;

    GridLayoutManager gridLayoutManager;

    int pageIndex = 1;

    MaterialHeader materialHeader;

    RentalsSunHeaderView header;

    boolean isRefresh;

    @AfterInject
    public void afterBaseInject() {
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
    }

    @AfterViews
    void afterRecyclerView() {
        if (bus != null) {
            bus.register(this);
        }
        ultimateRecyclerView.setHasFixedSize(true);
        setLayoutManager();
        if (layoutId > 0) {
            //设置视差header
            enableParallaxHeader(layoutId);
        }

        //设置空视图
        enableEmptyViewPolicy();

        //启用加载更多
        enableLoadMore();

        //获取数据
        afterLoadMore();

        //设置 Material下拉刷新
//        refreshingMaterial();
//        refreshingStringArray();
        refreshingRentalsSun();

//        ultimateRecyclerView.setItemViewCacheSize();
        setHorizontalDividerItemDecoration(35, 35);
        ultimateRecyclerView.setAdapter(myAdapter);
    }

    public void setHorizontalDividerItemDecoration(int leftMargin, int rightMargin) {
        paint.setStrokeWidth(1);
        paint.setColor(line_color);
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).margin(leftMargin, rightMargin).paint(paint).build());
    }

    public void setVerticalDividerItemDecoration(int leftMargin, int rightMargin) {
        paint.setStrokeWidth(1);
        paint.setColor(line_color);
        ultimateRecyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(this).margin(leftMargin, rightMargin).paint(paint).build());
    }

    public void enableLoadMore() {
        ultimateRecyclerView.setOnLoadMoreListener(new LLRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                if (myAdapter.getItems().size() >= myAdapter.getTotal()) {
//                    AndroidTool.showToast(BaseUltimateRecyclerViewActivity.this, "没有更多的数据了！~");
                    ultimateRecyclerView.disableLoadmore();
                } else {
                    pageIndex++;
                    afterLoadMore();
                }
            }
        });
        ultimateRecyclerView.reenableLoadmore();
    }

    /**
     * set layoutManager
     * you can  override
     */
    public void setLayoutManager() {
        verticalItem();
    }

    /**
     * 设置EmptyView
     */
    public void enableEmptyViewPolicy() {
        //  ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_KEEP_HEADER_AND_LOARMORE);
        //    ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_KEEP_HEADER);
        //  ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
//        ultimateRecyclerView.setEmptyView(R.layout.empty_view, HFRefreshRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
    }

    //线性布局
    public void verticalItem() {
        myAdapter.verticalAndHorizontal = BaseUltimateRecyclerViewAdapter.VerticalAndHorizontal.Vertical;
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);

    }

    //网格布局
    public void horizontalItem() {
        myAdapter.verticalAndHorizontal = BaseUltimateRecyclerViewAdapter.VerticalAndHorizontal.Horizontal;
        ultimateRecyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * 设置 启用 ParallaxHeader（视差header）
     * you can override
     */
    public void enableParallaxHeader(int layoutId) {
        View view = layoutInflater.inflate(layoutId, ultimateRecyclerView.mRecyclerView, false);
        ultimateRecyclerView.setParallaxHeader(view);
        ultimateRecyclerView.setOnParallaxScroll(new LLRecyclerView.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {

            }
        });
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
                if (!ultimateRecyclerView.isLoadMoreEnabled())
                    ultimateRecyclerView.reenableLoadmore();
            } else {
                if (ultimateRecyclerView.isLoadMoreEnabled())
                    ultimateRecyclerView.disableLoadmore();
            }
        } else if (pageIndex == 1) {
            linearLayoutManager.scrollToPosition(0);
        }
    }

    public void refreshingRentalsSun() {
        //启用刷新
        ultimateRecyclerView.refreshingRentalsSun();
        header = new RentalsSunHeaderView(this);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, 15, 0, 10);
        header.setUp(ultimateRecyclerView.mPtrFrameLayout);
        ultimateRecyclerView.mPtrFrameLayout.setHeaderView(header);
        ultimateRecyclerView.mPtrFrameLayout.addPtrUIHandler(header);
        ultimateRecyclerView.mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                isRefresh = true;
                pageIndex = 1;
                afterLoadMore();
            }
        });
    }

    @Override
    public void finish() {
        bus.unregister(this);
        super.finish();
    }

}
