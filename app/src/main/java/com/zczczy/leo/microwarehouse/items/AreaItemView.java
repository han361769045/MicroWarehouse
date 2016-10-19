package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.TextView;


import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.AreaModel;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/4.
 */
@EViewGroup(R.layout.activity_province_item)
public class AreaItemView extends ItemView<AreaModel> {

    @ViewById
    TextView txt_province;

    public AreaItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        txt_province.setText(_data.AreaName);
    }
}
