package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.GoodsTypeModel;
import com.zczczy.leo.microwarehouse.tools.StringUtils;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zhangyan on 2016/10/13.
 * 分类页面  二级列表
 */
@EViewGroup(R.layout.item_recommend_rv)
public class ClassifyComListItemView extends ItemView<GoodsTypeModel> {

    // 商品图片
    @ViewById
    ImageView comImage_Iv;

    // 商品名
    @ViewById
    TextView comName_Tv;

    public ClassifyComListItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {

        Glide.with(context)
                .load(_data.GoodsTypeIcon)
                .placeholder(R.drawable.goods_default)
                .into(comImage_Iv);

        comName_Tv.setText(StringUtils.isEmpty(_data.GoodsTypeName) ? "商品名" : _data.GoodsTypeName);
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
