package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.GoodsCommentsModel;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.StringUtils;


/**
 * Created by Leo on 2016/5/1.
 */
@EViewGroup(R.layout.activity_goods_comments_item)
public class GoodsCommentsItemView extends ItemView<GoodsCommentsModel> {

    @ViewById
    ImageView img_avatar;

    @ViewById
    TextView txt_comments, txt_time;

    @ViewById
    RatingBar ratingBar;

    public GoodsCommentsItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
//        txt_name.setText(_data.UserLogin);
        txt_comments.setText(_data.GoodsCommentsNr);
        txt_time.setText(_data.PlTime);
        ratingBar.setRating(_data.XNum);
        if (!StringUtils.isEmpty(_data.HeadImg)) {
            Glide.with(context).load(_data.HeadImg)
                    .skipMemoryCache(true)
                    .crossFade()
                    .centerCrop()
                    .placeholder(R.drawable.default_header)
                    .error(R.drawable.default_header)
                    .into(img_avatar);
        }
    }
}
