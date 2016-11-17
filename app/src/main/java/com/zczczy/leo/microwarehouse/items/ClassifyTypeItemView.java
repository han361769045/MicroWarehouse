package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.ClassifyTypeAdapter;
import com.zczczy.leo.microwarehouse.fragments.ClassifyFragment;
import com.zczczy.leo.microwarehouse.model.ClassifyDataModel;
import com.zczczy.leo.microwarehouse.tools.StringUtils;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zhangyan on 2016/10/12.
 * 分类页面 商品分类
 */

@EViewGroup(R.layout.item_classify_type_rv)
public class ClassifyTypeItemView extends ItemView<ClassifyDataModel> {

    /**
     * 父布局
     */
    @ViewById
    RelativeLayout classifyBg_Rl;

    /**
     * 选中的光标
     */
    @ViewById
    View isSelect_View;

    /**
     * 类别名
     */
    @ViewById
    TextView classifyTitle_Tv;

    public ClassifyTypeItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        if (_data.isSelect) {
            isSelect_View.setVisibility(VISIBLE);
            classifyBg_Rl.setBackgroundResource(R.color.classify_title_bg);
            classifyTitle_Tv.setTextColor(ContextCompat.getColor(context, R.color.title_bar_bg));
        } else {
            isSelect_View.setVisibility(GONE);
            classifyBg_Rl.setBackgroundResource(R.color.classify_bg);
            classifyTitle_Tv.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        classifyTitle_Tv.setText(StringUtils.isEmpty(_data.GoodsTypeName) ? "" : _data.GoodsTypeName);
    }

    @Override
    public void onItemSelected() {
        /* nothing to do */
    }

    @Override
    public void onItemClear() {
        /* nothing to do */
    }
}
