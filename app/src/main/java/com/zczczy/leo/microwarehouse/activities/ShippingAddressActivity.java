package com.zczczy.leo.microwarehouse.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.adapters.BaseRecyclerViewAdapter;
import com.zczczy.leo.microwarehouse.adapters.ShippingAddressAdapter;
import com.zczczy.leo.microwarehouse.model.ShippingAddressModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by Leo on 2016/5/4.
 */
@EActivity(R.layout.activity_shipping_address)
public class ShippingAddressActivity extends BaseRecyclerViewActivity {


    @Bean(ShippingAddressAdapter.class)
    BaseRecyclerViewAdapter myAdapter;


    @Extra
    boolean isFinish;

    @AfterViews
    void afterView() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<ShippingAddressModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, ShippingAddressModel obj, int position) {
                if (isFinish) {
//                    Intent intent = new Intent();
//                    intent.putExtra("model", obj);
//                    setResult(1001, intent);
                    finish();
                }
            }
        });
    }

    @Click
    void btn_add_shipping_address() {
//        AddShippingAddressActivity_.intent(this).start();
    }


    public void onResume() {
        super.onResume();
        myAdapter.getMoreData();
    }


}
