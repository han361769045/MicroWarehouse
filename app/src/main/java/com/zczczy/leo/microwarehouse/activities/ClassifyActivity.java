package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.ClassifyComListAdapter;
import com.zczczy.leo.microwarehouse.adapters.ClassifyRecommendAdapter;
import com.zczczy.leo.microwarehouse.adapters.ClassifyTypeAdapter;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.BaseModelJsonDoubleT;
import com.zczczy.leo.microwarehouse.model.ClassifyDataModel;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;
import com.zczczy.leo.microwarehouse.model.RecommendComModel;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;
import com.zczczy.leo.microwarehouse.views.FullyGridLayoutManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

/**
 * Created by zhangyan on 2016/10/14.
 * 分类页面
 */
@EActivity(R.layout.activity_classify)
public class ClassifyActivity extends BaseActivity {

    @Extra
    String id;

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

    @Bean
    OttoBus bus;

    private int position;

    private List<ClassifyDataModel> list;


    @Override
    void baseAfterView() {

    }

    /**
     * 点击去搜索页面
     */
    @Click
    void myTitleBar() {
        SearchActivity_.intent(this).MouthSearch("0").start();
    }

    @AfterViews
    void afterView() {
        if (bus != null) {
            bus.register(this);
        }
        if (isNetworkAvailable()){

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
        GridLayoutManager manager = new GridLayoutManager(ClassifyActivity.this, 1);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        comTypeList_Rv.setLayoutManager(manager);
        comTypeList_Rv.setAdapter(typeAdapter);

        typeAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<ClassifyDataModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, ClassifyDataModel obj, int position) {
                /* 更新选中状态UI  并更新二级列表数据 */
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
        FullyGridLayoutManager manager = new FullyGridLayoutManager(ClassifyActivity.this, 3);
        moreCom_Rv.setLayoutManager(manager);
        moreCom_Rv.setAdapter(comListAdapter);

        comListAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<GoodsTypeModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, GoodsTypeModel obj, int position) {
                /* 点击进入商品详情页面 */
                CategoryActivity_.intent(ClassifyActivity.this).title(obj.GoodsTypeName).id(obj.GoodsTypeId).start();
            }
        });
    }

    /**
     * 初始化推荐列表
     */
    private void initRecommendRv() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(ClassifyActivity.this, 3);
        recommendCom_Rv.setLayoutManager(manager);
        recommendCom_Rv.setAdapter(recommendAdapter);

        recommendAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<RecommendComModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, RecommendComModel obj, int position) {
                /* 点击进入商品详情页面 */
                GoodsDetailActivity_.intent(ClassifyActivity.this).goodsId(obj.GoodsInfoId).start();
            }
        });
    }

    /**
     * 获取一级列表数据
     */
    @Background
    void getClassifyData() {
        BaseModelJson<List<ClassifyDataModel>> modelJson = client.getGoodsTypePid();
        list = modelJson.Data;
        afterGetData(typeAdapter, modelJson);

        /* 将上个页面点击的item设置为选中状态 并移动到对应的位置 */
        if (id == null){
            return;
        }
        if (list != null && list.size() > 0) {
            int count = list.size();
            for (int i = 0; i < count; i++) {
                if (list.get(i).GoodsTypeId.equals(id)) {
                    this.position = i;
                    list.get(i).isSelect = true;
                    getMoreComData(list.get(i).GoodsTypeId);
                }
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
     */
    @Subscribe
    public void notifyUI(Integer number) {
        if (number == 1002) {
            comTypeList_Rv.scrollToPosition(position);
        }
    }

    @UiThread
    void showIcon() {
        more_Iv.setVisibility(View.VISIBLE);
        hot_Iv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bus != null) {
            bus.unregister(this);
        }
    }
}
