package com.zczczy.leo.microwarehouse.activities;

import android.graphics.drawable.ColorDrawable;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.View;

import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;

import com.zczczy.leo.microwarehouse.fragments.CommonCategoryFragment;
import com.zczczy.leo.microwarehouse.fragments.CommonCategoryFragment_;

import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.tools.DensityUtil;

import org.androidannotations.annotations.AfterViews;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import org.androidannotations.annotations.ViewById;

import org.springframework.util.StringUtils;



/**
 * Created by Leo on 2016/5/21.
 * 商品列表
 */
@EActivity(R.layout.activity_category)
public class CategoryActivity extends BaseActivity {

    @Extra
    String id, title;

    FragmentManager fragmentManager;

    CommonCategoryFragment commonCategoryFragment;

    @ViewById
    RadioButton rb_price, rb_filter, rb_sell_count;

    @ViewById
    RadioGroup radio_group;

    @ViewById
    TextView titleName_Tv;

    String priceMin, priceMax;

    EditText edt_min_price, edt_max_price;

//    boolean isRefresh;

    PopupWindow popupWindow;

    View view;

    String orderBy = Constants.SELL_COUNT;

    boolean isSelected;


    @Override
    void baseAfterView() {

    }

    /**
     * 点击返回
     */
    @Click
    void titleName_Tv() {
        finish();
    }

    /**
     * 点击去搜索
     */
    @Click
    void search_Tv() {
        SearchActivity_.intent(this).start();
    }

    @AfterViews
    void afterView() {
        if (!StringUtils.isEmpty(title)) {
            titleName_Tv.setText(title);
        }
        fragmentManager = getSupportFragmentManager();
        // 默认按照销量优先加载
        rb_sell_count(true);
    }

    /**
     * 填写价格筛选
     */
    @Click
    void rb_filter() {
        if (rb_filter.isChecked()) {
            orderBy = Constants.PRICE_FILTER;
//            isRefresh = true;
            showProperties();
        }
    }

    /**
     * 按 最高/最低 价筛选
     */
    @Click
    void rb_price() {
        if (rb_price.isChecked() && isSelected) {
//            isRefresh = true;
            isSelected = false;
            rb_price.setSelected(isSelected);
            orderBy = Constants.PRICE_ASC;
            changeFragment();
        } else if (rb_price.isChecked() && !isSelected) {
//            isRefresh = true;
            isSelected = true;
            rb_price.setSelected(isSelected);
            orderBy = Constants.PRICE_DESC;
            changeFragment();
        }
    }

    /**
     * 按销量筛选
     * @param isChecked 是否是选中状态
     */
    @CheckedChange
    void rb_sell_count(boolean isChecked) {
        if (isChecked) {
            orderBy = Constants.SELL_COUNT;
//            isRefresh = true;
            changeFragment();
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
                    priceMin = "".equals(edt_min_price.getText().toString()) ? "0" : edt_min_price.getText().toString();
                    priceMax = "".equals(edt_max_price.getText().toString()) ? "0" : edt_max_price.getText().toString();
                    closeInputMethod(view);
                    popupWindow.dismiss();
                    changeFragment();
                }
            });
            popupWindow = new PopupWindow(view, DensityUtil.dip2px(this, 220), DensityUtil.dip2px(this, 110), true);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            //设置SelectPicPopupWindow弹出窗体的背景
            popupWindow.setBackgroundDrawable(dw);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    closeInputMethod(view);
                }
            });
            popupWindow.setOutsideTouchable(true);
        }
        popupWindow.showAsDropDown(rb_filter);
    }

    /**
     * 按照筛选条件不同 切换fragment
     */
    void changeFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        commonCategoryFragment = CommonCategoryFragment_.builder().goodsId(id + "").orderBy(orderBy).priceMin(priceMin).priceMax(priceMax).build();
        transaction.replace(R.id.common_fragment, commonCategoryFragment);
        transaction.commit();
    }


}
