package com.zczczy.leo.microwarehouse.items;

import android.content.Context;

import com.zczczy.leo.microwarehouse.R;

import org.androidannotations.annotations.EViewGroup;

/**
 * Created by zczczy on 2016/5/21.
 */
@EViewGroup(R.layout.item_snacks_layout)
public class SnacksItemView extends ItemView {
    public SnacksItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {

    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
