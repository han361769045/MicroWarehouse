package com.zczczy.leo.microwarehouse.fragments.lotteryfragments;

import android.graphics.Color;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.CustomUltimateRecyclerview;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.BasicGridLayoutManager;
import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;
import com.zczczy.leo.microwarehouse.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.lotteryadapters.LotteryHomeAdapter;
import com.zczczy.leo.microwarehouse.fragments.FindFragment;
import com.zczczy.leo.microwarehouse.items.BaseUltimateViewHolder;
import com.zczczy.leo.microwarehouse.items.LotteryHomeAdvertisementItemView;
import com.zczczy.leo.microwarehouse.items.LotteryHomeAdvertisementItemView_;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by zczczy on 2016/11/10.
 * 夺宝首页
 */
@EFragment(R.layout.lottery_home_fragment)
public class LotteryHomeFragment extends LotteryUltimateRecyclerViewFragment<GoodsModel> {

    @ViewById
    CustomUltimateRecyclerview ultimate_recycler_view;

    BasicGridLayoutManager gridLayoutManager;


    @Bean
    void myAdapter(LotteryHomeAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

   @AfterViews
   void afterView(){
       bus.register(this);
       myTitleBar.setTitle("一元夺宝");

       setHomeTitle();
       ultimate_recycler_view.setHasFixedSize(true);
       gridLayoutManager = new BasicGridLayoutManager(getActivity(), 2, OrientationHelper.VERTICAL, false, myAdapter);
       ultimate_recycler_view.setLayoutManager(gridLayoutManager);
       ultimate_recycler_view.setAdapter(myAdapter);
       ultimate_recycler_view.enableLoadmore();
       myAdapter.setCustomLoadMoreView(R.layout.custom_bottom_progressbar);
       afterLoadMore();
       ultimate_recycler_view.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
           @Override
           public void loadMore(int itemsCount, int maxLastVisiblePosition) {
               if (myAdapter.getItems().size() >= myAdapter.getTotal()) {
//                    AndroidTool.showToast(HomeFragment.this, "没有更多的数据了！~");
                   ultimate_recycler_view.disableLoadmore();
                   myAdapter.notifyItemRemoved(itemsCount > 0 ? itemsCount - 1 : 0);
               } else {
                   pageIndex++;
                   afterLoadMore();
               }
           }
       });
       ultimate_recycler_view.setNormalHeader(LotteryHomeAdvertisementItemView_.build(getActivity()));
       ultimate_recycler_view.setCustomSwipeToRefresh();
       refreshingMaterial();
       paint.setStrokeWidth(0);
       paint.setColor(Color.WHITE);
   }

    LotteryHomeAdvertisementItemView itemView;

    void setHomeTitle(){
        myAdapter.setBindHeaderViewHolder(new BaseUltimateRecyclerViewAdapter.BindHeaderViewHolder() {
            @Override
            public void onBindHeaderViewHolder(BaseUltimateViewHolder viewHolder) {
                UltimateRecyclerView.CustomRelativeWrapper customRelativeWrapper = (UltimateRecyclerView.CustomRelativeWrapper) viewHolder.itemView;
                itemView = (LotteryHomeAdvertisementItemView) (customRelativeWrapper.getChildAt(0));
                itemView.init(app.getAdvertModelList());
            }
        });
        myAdapter.setOnItemClickListener(new BaseUltimateRecyclerViewAdapter.OnItemClickListener<GoodsModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, GoodsModel obj, int position) {
                GoodsDetailActivity_.intent(LotteryHomeFragment.this).goodsId(obj.GoodsInfoId).start();
            }

            @Override
            public void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });
    }


    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, 4, isRefresh, 1);
    }
    void refreshingMaterial() {
        materialHeader = new MaterialHeader(getActivity());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        materialHeader.setColorSchemeColors(colors);
        materialHeader.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        materialHeader.setPadding(0, 15, 0, 10);
        materialHeader.setPtrFrameLayout(ultimate_recycler_view.mPtrFrameLayout);
        ultimate_recycler_view.mPtrFrameLayout.autoRefresh(false);
        ultimate_recycler_view.mPtrFrameLayout.setHeaderView(materialHeader);
        ultimate_recycler_view.mPtrFrameLayout.addPtrUIHandler(materialHeader);
        ultimate_recycler_view.mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean g = PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                if (g) {
                    myTitleBar.setVisibility(View.GONE);
                } else {
                    myTitleBar.setVisibility(View.VISIBLE);
                }
                return g;
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
            gridLayoutManager.scrollToPosition(0);
            ultimate_recycler_view.mPtrFrameLayout.refreshComplete();
            isRefresh = false;
            if (myAdapter.getItems().size() < myAdapter.getTotal()) {
                ultimate_recycler_view.reenableLoadmore(layoutInflater.inflate(R.layout.custom_bottom_progressbar, null));
            } else {
                ultimate_recycler_view.disableLoadmore();
            }
        } else if (pageIndex == 1) {
            gridLayoutManager.scrollToPosition(0);
        }
        showTitleBar();
    }

    @UiThread(delay = 1000)
    void showTitleBar() {
        myTitleBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            bus.unregister(this);
            if (itemView != null) {
                itemView.stopAutoCycle();
            }
        } else {
            bus.register(this);
            if (itemView != null) {
                itemView.startAutoCycle();
            }
        }
    }

}
