package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.AddShippingAddressActivity_;
import com.zczczy.leo.microwarehouse.activities.ShippingAddressActivity;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.ShippingAddressModel;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    ShippingAddressActivity context;

    public ShippingAddressItemView(Context context) {
        super(context);
        this.context = (ShippingAddressActivity) context;
    }

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @Override
    protected void init(Object... objects) {
        receipt_name.setText(_data.ReceiptName);
        receipt_phone.setText(_data.Mobile);
        receipt_wholeaddress.setText(_data.ProvinceName.concat("-" + _data.CityName + "-" + _data.AreaName + _data.DetailAddress));
        txt_default.setChecked("1".equals(_data.IsPrimary));
    }


    @Click
    void txt_edit() {
        AddShippingAddressActivity_.intent(context).receiptAddressId(_data.MReceiptAddressId).startForResult(1000);
    }

    @Click
    void txt_default() {
        if (txt_default.isChecked()) {
            setDefaultShippingAddress();
        } else {
            txt_default.setChecked(true);
        }
    }

    @Background
    void setDefaultShippingAddress() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterSetDefaultShippingAddress(myRestClient.updDefaultReceiptAddress(_data.MReceiptAddressId));
    }

    @UiThread
    void afterSetDefaultShippingAddress(BaseModel bm) {
        if (bm == null) {
            AndroidTool.showToast(context, no_net);
        } else if (!bm.Successful) {
            AndroidTool.showToast(context, bm.Error);
        } else {
            for (ShippingAddressModel mReceiptAddressModel : baseRecyclerViewAdapter.getItems()) {
                mReceiptAddressModel.IsPrimary = "0";
            }
            _data.IsPrimary = "1";
            Collections.swap(baseRecyclerViewAdapter.getItems(), 0, viewHolder.getAdapterPosition());
            baseRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Click
    void txt_delete() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle("提示").setMessage("确定要删除吗？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete();
            }
        }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
    }

    @Background
    void delete() {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterDelete(myRestClient.delReceiptAddress(_data.MReceiptAddressId));
    }

    @UiThread
    void afterDelete(BaseModel bm) {
        if (bm == null) {
            AndroidTool.showToast(context, no_net);
        } else if (!bm.Successful) {
            AndroidTool.showToast(context, bm.Error);
        } else {
            baseRecyclerViewAdapter.deleteItem(_data, viewHolder.getAdapterPosition());
        }
    }
}
