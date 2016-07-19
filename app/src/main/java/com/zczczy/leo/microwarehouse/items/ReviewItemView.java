package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.StringUtils;

/**
 * Created by leo on 2016/5/8.
 */
@EViewGroup(R.layout.activity_review_item)
public class ReviewItemView extends ItemView<OrderDetailModel> {

    @ViewById
    ImageView img_avatar;

    @ViewById
    TextView txt_name, txt_des;


    public ReviewItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        if (!StringUtils.isEmpty(_data.GoodsImgSl)) {
            Glide.with(context)
                    .load(_data.GoodsImgSl)
                    .skipMemoryCache(true)
                    .crossFade()
                    .centerCrop()
                    .placeholder(R.drawable.goods_default)
                    .error(R.drawable.goods_default)
                    .into(img_avatar);
        }
        txt_name.setText(_data.ProductName);
//        txt_des.setText(_data.GoodsDesc);
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
