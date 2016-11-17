package com.zczczy.leo.microwarehouse.items;

import android.content.Context;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.GoodsModel;

import org.androidannotations.annotations.EViewGroup;

/**
 * Created by zczczy on 2016/9/29.
 */
@EViewGroup(R.layout.item_normal_bg)
public class ItemHeaderBg extends ItemView<GoodsModel> {
    public ItemHeaderBg(Context context) {
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
