package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.TextView;


import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by leo on 2016/5/4.
 */
@EViewGroup(R.layout.activity_category_item)
public class CategoryItemView extends ItemView<GoodsTypeModel> {

    @ViewById
    TextView txt_first_category;

    public CategoryItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        txt_first_category.setText(_data.GoodsTypeName);
        txt_first_category.setSelected(_data.isSelected);
    }
}
