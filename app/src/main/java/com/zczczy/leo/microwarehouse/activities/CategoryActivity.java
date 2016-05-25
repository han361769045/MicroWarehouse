package com.zczczy.leo.microwarehouse.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;

import com.squareup.otto.Subscribe;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.CategoryAdapter;
import com.zczczy.leo.microwarehouse.fragments.CommonCategoryFragment;
import com.zczczy.leo.microwarehouse.fragments.CommonCategoryFragment_;
import com.zczczy.leo.microwarehouse.listener.OttoBus;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by Leo on 2016/5/21.
 */
@EActivity(R.layout.activity_category)
public class CategoryActivity extends BaseRecyclerViewActivity<GoodsTypeModel> {

    @Bean
    OttoBus bus;

    @Extra
    String id;

    FragmentManager fragmentManager;

    CommonCategoryFragment commonCategoryFragment;

    @Bean
    void setMyAdapter(CategoryAdapter myAdapter) {
        this.myAdapter = myAdapter;
        fragmentManager = getSupportFragmentManager();
    }

    @AfterViews
    void afterView() {
        myAdapter.getMoreData(id);
        myAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<GoodsTypeModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, GoodsTypeModel obj, int position) {
                if (!obj.isSelected) {
                    changeFragment(obj);
                }
            }
        });
    }


    @Subscribe
    public void notifyUI(BaseModel bm) {
        if (bm.Successful) {
            changeFragment(myAdapter.getItemData(0));
        }
    }

    void changeFragment(GoodsTypeModel model) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int firstPosition = 0;
        int lastPosition = 0;
        for (int i = 0; i < myAdapter.getItems().size(); i++) {
            GoodsTypeModel tempModel = myAdapter.getItems().get(i);
            if (tempModel.GoodsTypeId == model.GoodsTypeId) {
                firstPosition = i;
            }
            if (tempModel.isSelected) {
                lastPosition = i;
                tempModel.isSelected = false;
            }
        }
        model.isSelected = true;
        myAdapter.notifyItemChanged(firstPosition);
        myAdapter.notifyItemChanged(lastPosition);
//        AndroidTool.showToast(this, firstPosition + "=====" + lastPosition);
        linearLayoutManager.scrollToPosition(firstPosition);
        commonCategoryFragment = CommonCategoryFragment_.builder().goodsId(model.GoodsTypeId + "").build();
        transaction.replace(R.id.common_fragment, commonCategoryFragment);
        transaction.commit();
    }


    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

}
