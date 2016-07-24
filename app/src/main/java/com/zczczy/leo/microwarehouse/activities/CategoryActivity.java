package com.zczczy.leo.microwarehouse.activities;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.CategoryAdapter;
import com.zczczy.leo.microwarehouse.fragments.CommonCategoryFragment;
import com.zczczy.leo.microwarehouse.fragments.CommonCategoryFragment_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.tools.DensityUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Leo on 2016/5/21.
 */
@EActivity(R.layout.activity_category)
public class CategoryActivity extends BaseRecyclerViewActivity<GoodsTypeModel> {

    @Extra
    String id, title;

    FragmentManager fragmentManager;

    CommonCategoryFragment commonCategoryFragment;

    @ViewById
    RadioButton rb_price, rb_filter;

    @ViewById
    RadioGroup radio_group;

    EditText edt_min_price, edt_max_price;

    boolean isRefresh;

    PopupWindow popupWindow;

    View view;

    String orderBy = Constants.ZONG_HE;

    boolean isSelected;

    @Click
    void rb_filter() {
        if (rb_filter.isChecked()) {
            showProperties();
        }
    }


    @Click
    void rb_price() {
        if (rb_price.isChecked() && isSelected) {
            isRefresh = true;
            isSelected = false;
            rb_price.setSelected(isSelected);
            orderBy = Constants.PRICE_ASC;
//            afterLoadMore();
        } else if (rb_price.isChecked() && !isSelected) {
            isRefresh = true;
            isSelected = true;
            rb_price.setSelected(isSelected);
            orderBy = Constants.PRICE_DESC;
            //            afterLoadMore();
        }
    }

    @CheckedChange
    void rb_sell_count(boolean isChecked) {
        if (isChecked) {
            orderBy = Constants.SELL_COUNT;
            isRefresh = true;
//            afterLoadMore();
        }
    }


    void showProperties() {
        if (popupWindow == null) {
            view = layoutInflater.inflate(R.layout.filter_popup, null);
            edt_min_price = (EditText) view.findViewById(R.id.edt_min_price);
            edt_max_price = (EditText) view.findViewById(R.id.edt_max_price);
            view.findViewById(R.id.txt_reset).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edt_min_price.setText("");
                    edt_max_price.setText("");
                }
            });
            view.findViewById(R.id.txt_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AndroidTool.showToast(CategoryActivity.this, "1111111");
                }
            });


            popupWindow = new PopupWindow(view, DensityUtil.dip2px(this, 220), DensityUtil.dip2px(this, 110), true);
//            //实例化一个ColorDrawable颜色为半透明
//            ColorDrawable dw = new ColorDrawable(0xb0000000);
//            //设置SelectPicPopupWindow弹出窗体的背景
//            popupWindow.setBackgroundDrawable(dw);
        }
        popupWindow.showAsDropDown(rb_filter);
    }

    @Bean
    void setMyAdapter(CategoryAdapter myAdapter) {
        this.myAdapter = myAdapter;
        fragmentManager = getSupportFragmentManager();
    }

    @AfterViews
    void afterView() {
        if (!StringUtils.isEmpty(title)) {
            myTitleBar.setTitle(title);
        }
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

    public void notifyUI(BaseModelJson<List<GoodsTypeModel>> bm) {
        if (bm.Successful) {
            if (myAdapter.getItemData(0) != null) {
                changeFragment(myAdapter.getItemData(0));
            }
        }
    }

    void changeFragment(GoodsTypeModel model) {
        if (edt_min_price != null) {
            edt_min_price.setText("");
        }
        if (edt_max_price != null) {
            edt_max_price.setText("");
        }

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
}
