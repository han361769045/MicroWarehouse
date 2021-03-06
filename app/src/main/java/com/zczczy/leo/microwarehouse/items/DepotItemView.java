package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.DepotModel;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.StringUtils;

/**
 * @author Created by LuLeo on 2016/6/15.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/15.
 */
@EViewGroup(R.layout.activity_depot_item)
public class DepotItemView extends ItemView<DepotModel> {

    @ViewById
    TextView txt_depot_name, txt_depot_type, txt_depot_address;

    @ViewById
    ImageView img_depot;

    public DepotItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {

        txt_depot_name.setText(_data.DepotName);
        txt_depot_type.setText(_data.DepotType);
        txt_depot_address.setText(_data.Address);

        if (!StringUtils.isEmpty(_data.DepotImgUrl)) {
            Glide.with(context)
                    .load(_data.DepotImgUrl)
                    .placeholder(R.drawable.goods_default)
                    .error(R.drawable.goods_default)
                    .skipMemoryCache(true)
                    .crossFade()
                    .centerCrop()
                    .into(img_depot);
        }
    }
}
