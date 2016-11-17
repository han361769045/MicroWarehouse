package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.RecommendComModel;
import com.zczczy.leo.microwarehouse.tools.StringUtils;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zhangyan on 2016/10/13.
 * 分类页面  推荐商品列表
 */
@EViewGroup(R.layout.item_recommend_rv)
public class RecommendItemView extends ItemView<RecommendComModel> {

    // 商品图片
    @ViewById
    ImageView comImage_Iv;

    // 商品名
    @ViewById
    TextView comName_Tv;

    public RecommendItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        Glide.with(context).load(_data.GoodsImgSl).placeholder(R.drawable.goods_default).into(comImage_Iv);

        comName_Tv.setText(StringUtils.isEmpty(_data.GodosName) ? "商品名":_data.GodosName); // TODO: 2016/10/13 测试文字 删
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
