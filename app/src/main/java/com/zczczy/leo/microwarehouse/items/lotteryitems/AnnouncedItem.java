package com.zczczy.leo.microwarehouse.items.lotteryitems;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.items.ItemView;
import com.zczczy.leo.microwarehouse.model.GoodsModel;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.viewgroup.MyProgressBar;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

/**
 * Created by zczczy on 2016/11/17.
 */
@EViewGroup(R.layout.item_announced)
public class AnnouncedItem  extends ItemView<GoodsModel>{
    @ViewById
    ImageView pic;

    @ViewById
    Button img_add_cart;

    @StringRes
    String no_net;

    @ViewById
    TextView goods_name,total_people,more_people;

    @ViewById
    MyProgressBar pb_progressbar;

    @ViewById
    LinearLayout ll_bat_price, ll_price, ll_delete_price,ll_activity_now,ll_complete;

    @Pref
    MyPrefs_ pre;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;



    public AnnouncedItem(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        pb_progressbar.setProgress(81);
        pb_progressbar.setMax(120);
        ll_activity_now.setVisibility(GONE);
        ll_complete.setVisibility(VISIBLE);
        img_add_cart.setBackgroundResource(R.drawable.lottery_grab_gray);
        if (!StringUtils.isEmpty(_data.GoodsImgSl)) {
            Glide.with(context).load(_data.GoodsImgSl)
                    .skipMemoryCache(true)
                    .crossFade().
                    centerCrop().placeholder(R.drawable.goods_default).error(R.drawable.goods_default).into(pic);
        }

        goods_name.setText(_data.GodosName);
        total_people.setText(_data.GoodsPrice);
        more_people.setText(_data.GoodsPrice);
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
