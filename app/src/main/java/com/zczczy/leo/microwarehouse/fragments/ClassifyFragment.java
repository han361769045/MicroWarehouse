package com.zczczy.leo.microwarehouse.fragments;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.CategoryActivity_;
import com.zczczy.leo.microwarehouse.activities.GoodsDetailActivity_;
import com.zczczy.leo.microwarehouse.activities.SearchActivity_;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.ClassifyComListAdapter;
import com.zczczy.leo.microwarehouse.adapters.ClassifyRecommendAdapter;
import com.zczczy.leo.microwarehouse.adapters.ClassifyTypeAdapter;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.BaseModelJsonDoubleT;
import com.zczczy.leo.microwarehouse.model.ClassifyDataModel;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;
import com.zczczy.leo.microwarehouse.model.RecommendComModel;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;
import com.zczczy.leo.microwarehouse.views.FullyGridLayoutManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

/**
 * Created by zhangyan on 2016/10/12.
 * 商品分类
 */
@EFragment(R.layout.fragment_classify)
public class ClassifyFragment extends BaseFragment {

    @ViewById
    MyTitleBar myTitleBar;

    /**
     * 一级列表
     */
    @ViewById
    RecyclerView comTypeList_Rv;

    /**
     * 二级列表
     */
    @ViewById
    RecyclerView moreCom_Rv;

    /**
     * 推荐列表
     */
    @ViewById
    RecyclerView recommendCom_Rv;

    /**
     * 更多(图标)
     */
    @ViewById
    ImageView more_Iv;

    /**
     * 更多(图标)
     */
    @ViewById
    ImageView hot_Iv;

    /**
     * 一级列表适配器
     */
    @Bean
    ClassifyTypeAdapter typeAdapter;

    /**
     * 二级列表适配器
     */
    @Bean
    ClassifyComListAdapter comListAdapter;

    /**
     * 推荐列表适配器
     */
    @Bean
    ClassifyRecommendAdapter recommendAdapter;

    @RestService
    MyRestClient client;

    private List<ClassifyDataModel> list;

    /**
     * 点击去搜索页面
     */
    @Click
    void myTitleBar() {
        SearchActivity_.intent(this).MouthSearch("0").start();
    }

    @AfterViews
    void afterView() {
        AndroidTool.showLoadDialog(this);
        if (isNetworkAvailable(getActivity())){
            initTypeListRv();
            initMoreComRv();
            initRecommendRv();

        /* 获取数据 */
            getClassifyData();
        }
    }

    /**
     * 初始化一级列表
     */
    private void initTypeListRv() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        comTypeList_Rv.setLayoutManager(manager);
        comTypeList_Rv.setAdapter(typeAdapter);

        typeAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<ClassifyDataModel>() {

            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, ClassifyDataModel obj, int position) {
                /* 更新选中状态UI  并更新二级列表数据 */
                AndroidTool.showLoadDialog(getActivity());
                comListAdapter.clear();
                recommendAdapter.clear();

                for (int i = 0; i < list.size(); i++) {
                    list.get(i).isSelect = false;
                }
                obj.isSelect = true;
                typeAdapter.notifyDataSetChanged();

                getMoreComData(obj.GoodsTypeId);
            }
        });
    }

    /**
     * 初始化二级列表
     */
    private void initMoreComRv() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getActivity(), 3);
        moreCom_Rv.setLayoutManager(manager);
        moreCom_Rv.setAdapter(comListAdapter);

        comListAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<GoodsTypeModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, GoodsTypeModel obj, int position) {
                /* 点击进入商品详情页面 */
                CategoryActivity_.intent(getActivity()).title(obj.GoodsTypeName).id(obj.GoodsTypeId).start();
            }
        });
    }

    /**
     * 初始化推荐列表
     */
    private void initRecommendRv() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getActivity(), 3);
        recommendCom_Rv.setLayoutManager(manager);
        recommendCom_Rv.setAdapter(recommendAdapter);

        recommendAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<RecommendComModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, RecommendComModel obj, int position) {
                /* 点击进入商品详情页面 */
                GoodsDetailActivity_.intent(getActivity()).goodsId(obj.GoodsInfoId).start();
            }
        });
    }

    /**
     * 获取一级列表数据
     *
     * 这里要将列表中默认position的item设置为选中状态
     */
    @Background
    void getClassifyData() {
        BaseModelJson<List<ClassifyDataModel>> modelJson = client.getGoodsTypePid();
        list = modelJson.Data;
        if (list != null && list.size() > 0) {
            /* 这里要将列表中第一条item设置为选中状态 并移动到对应的item */
            list.get(0).isSelect = true;
            afterGetData(typeAdapter, modelJson);
            selectCurrentPosition(comTypeList_Rv, 0);

            /* 获取成功后 去获取二级列表的数据 */
            if (list != null && list.size() > 0) {
                getMoreComData(list.get(0).GoodsTypeId);
            }
        }

    }

    /**
     * 获取二级列表数据
     *
     * @param comId 商品类别ID
     */
    @Background
    void getMoreComData(String comId) {
        BaseModelJsonDoubleT<List<GoodsTypeModel>, List<RecommendComModel>> model = client.getGoodsTypeListByPid(comId);
        showIcon();
        afterGetData(comListAdapter, model);
        afterGetData(recommendAdapter, model);
    }

    /**
     * 向适配器中添加数据
     *
     * @param adapter 要添加数据的适配器
     * @param json    接口返回的数据(最外层)
     */
    @UiThread
    void afterGetData(BaseRecyclerViewAdapter adapter, BaseModel json) {
        adapter.getMoreData(json);
    }

    /**
     * 让一级列表选中需要显示的item
     * @param recyclerView 对应的RecyclerView
     * @param position item位置
     */
    @UiThread
    void selectCurrentPosition(RecyclerView recyclerView, int position) {
        recyclerView.scrollToPosition(position);
    }


    @UiThread
    void showIcon() {
        more_Iv.setVisibility(View.VISIBLE);
        hot_Iv.setVisibility(View.VISIBLE);
    }


}
