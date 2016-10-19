package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * @author Created by LuLeo on 2016/7/28.
 *         you can contact me at :361769045@qq.com
 * @since 2016/7/28.
 */
@EViewGroup(R.layout.actiivity_hot_search_item)
public class HotSearchItemView extends ItemView<String> {

    @ViewById
    TextView txt_hot_name;

    public HotSearchItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        txt_hot_name.setText(_data);
    }

}
