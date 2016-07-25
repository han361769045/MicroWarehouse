package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.GoodsAdapter;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.tools.DensityUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/21.
 */
@EActivity(R.layout.activity_search_result)
public class SearchResultActivity extends BaseUltimateRecyclerViewActivity<GoodsModel> {

    @Extra
    String searchContent;

    @ViewById
    TextView text_search;

    @ViewById
    RadioButton rb_price, rb_filter;

    String priceMin, priceMax;

    View view;

    EditText edt_min_price, edt_max_price;

    PopupWindow popupWindow;

    boolean isSelected;

    String orderBy = Constants.PRICE_FILTER;

    @Bean
    void myAdapter(GoodsAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @AfterViews
    void afterView() {
        text_search.setText(searchContent);
        empty_view.setText(empty_search);
        myAdapter.setOnItemClickListener(new BaseUltimateRecyclerViewAdapter.OnItemClickListener<GoodsModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, GoodsModel obj, int position) {
                GoodsDetailActivity_.intent(SearchResultActivity.this).goodsId(obj.GoodsInfoId).start();

            }

            @Override
            public void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });
        myTitleBar.setCustomViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myTitleBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        isSelected = true;
    }

    @Click
    void rb_filter() {
        if (rb_filter.isChecked()) {
            orderBy = Constants.PRICE_FILTER;
            isRefresh = true;
            showProperties();
        }
    }


    @CheckedChange
    void rb_sell_count(boolean isChecked) {
        if (isChecked) {
            orderBy = Constants.SELL_COUNT;
            isRefresh = true;
            afterLoadMore();
        }
    }

    @Click
    void rb_price() {
        if (rb_price.isChecked() && isSelected) {
            isRefresh = true;
            isSelected = false;
            rb_price.setSelected(isSelected);
            orderBy = Constants.PRICE_ASC;
            afterLoadMore();

        } else if (rb_price.isChecked() && !isSelected) {
            isRefresh = true;
            isSelected = true;
            rb_price.setSelected(isSelected);
            orderBy = Constants.PRICE_DESC;
            afterLoadMore();
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
                    priceMin = edt_min_price.getText().toString();
                    priceMax = edt_max_price.getText().toString();
                    closeInputMethod(view);
                    popupWindow.dismiss();
                    afterLoadMore();
                }
            });
//            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupWindow = new PopupWindow(view, DensityUtil.dip2px(this, 220), DensityUtil.dip2px(this, 110), true);
//            //实例化一个ColorDrawable颜色为半透明
//            ColorDrawable dw = new ColorDrawable(0xb0000000);
//            //设置SelectPicPopupWindow弹出窗体的背景
//            popupWindow.setBackgroundDrawable(dw);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    closeInputMethod(view);
                }
            });
        }
        popupWindow.showAsDropDown(rb_filter);
    }


    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, Constants.PAGE_COUNT, isRefresh, 0, searchContent, orderBy, priceMin, priceMax);
    }


    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
