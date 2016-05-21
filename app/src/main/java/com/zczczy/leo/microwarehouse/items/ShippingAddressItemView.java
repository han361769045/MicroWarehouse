package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.ShippingAddressModel;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by Leo on 2016/5/21.
 */

@EViewGroup(R.layout.activity_shipping_address_item)
public class ShippingAddressItemView extends ItemView<ShippingAddressModel> {


    @ViewById
    TextView receipt_name, receipt_phone, receipt_wholeaddress;

    @ViewById
    CheckBox txt_default;

    @Bean
    MyErrorHandler myErrorHandler;

    @RestService
    MyRestClient myRestClient;

    @Pref
    MyPrefs_ pre;

    @StringRes
    String no_net;


    Context context;

    public ShippingAddressItemView(Context context) {
        super(context);
        this.context = context;
    }

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @Override
    protected void init(Object... objects) {
        receipt_name.setText(_data.ReceiptName);
        receipt_phone.setText(_data.Mobile);
        receipt_wholeaddress.setText(_data.ProvinceName + "-" + _data.CityName + "-" + _data.AreaName + _data.DetailAddress);
        txt_default.setChecked("1".equals(_data.IsPrimary));

    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
