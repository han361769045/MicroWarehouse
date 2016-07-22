package com.zczczy.leo.microwarehouse.items;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.CartModel;
import com.zczczy.leo.microwarehouse.prefs.MyPrefs_;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import me.himanshusoni.quantityview.QuantityView;

/**
 * Created by Leo on 2016/5/21.
 */
@EViewGroup(R.layout.fragment_cart_item)
public class CartItemView extends ItemView<CartModel> implements QuantityView.OnQuantityChangeListener {

    @ViewById
    CheckBox cb_select;

    @ViewById
    ImageView img_cart_goods_img;

    @ViewById
    TextView txt_goods_name, txt_goods_price, txt_attr;

    @ViewById
    QuantityView quantityView;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @Pref
    MyPrefs_ pre;

    public CartItemView(Context context) {
        super(context);
    }

    @AfterInject
    void setMyErrorHandler() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        quantityView.setQuantityClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        quantityView.setOnQuantityChangeListener(this);
    }

    @Override
    protected void init(Object... objects) {
        if (!StringUtils.isEmpty(_data.GoodsImgSl)) {
            Glide.with(context)
                    .load(_data.GoodsImgSl)
                    .crossFade()
                    .centerCrop()
                    .placeholder(R.drawable.goods_default)
                    .error(R.drawable.goods_default)
                    .bitmapTransform(new ContrastFilterTransformation(context, 2.0f))
                    .into(img_cart_goods_img);
        }
        quantityView.setMaxQuantity(_data.GoodsStock);
        _data.ProductCount = _data.ProductCount > _data.GoodsStock ? _data.GoodsStock : _data.ProductCount;
        quantityView.setQuantity(_data.ProductCount);
        txt_attr.setText(_data.GoodsAttributeName);
        txt_goods_name.setText(_data.GodosName);
        txt_goods_price.setText(String.valueOf(_data.GoodsPrice));
        cb_select.setChecked(_data.isChecked);
    }

    @Click
    void cb_select() {
        _data.isChecked = cb_select.isChecked();
        //通知adapter
        baseRecyclerViewAdapter.itemNotify(_data.isChecked);
    }


    @Override
    public void onQuantityChanged(int newQuantity, boolean programmatically) {
        //点击+，-
        if (!programmatically) {
            AndroidTool.showLoadDialog(context);
            if (newQuantity > _data.ProductCount) {
                addShoppingCart(newQuantity);
            } else {
                subShoppingCart(newQuantity);
            }
        } else {
            //达到最大数或者最小数，或者增加删除失败 调用
            changeItem(newQuantity);
        }
    }

    @Background(serial = "subShopping")
    void subShoppingCart(int newQuantity) {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        afterAubShoppingCart(myRestClient.subShoppingCart(_data.GoodsInfoId), newQuantity);
    }

    @UiThread
    void afterAubShoppingCart(BaseModel bm, int newQuantity) {
        AndroidTool.dismissLoadDialog();
        if (bm == null) {
            AndroidTool.showToast(context, "修改失败");
        } else if (!bm.Successful) {
            AndroidTool.showToast(context, bm.Error);
        } else {
            _data.ProductCount--;
            changeItem(newQuantity);
        }
    }

    @Background(serial = "addShopping")
    void addShoppingCart(int newQuantity) {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        Map<String, String> map = new HashMap<>(3);
        map.put("GoodsInfoId", _data.GoodsInfoId);
        map.put("ProductCount", "1");
        map.put("GoodsAttributeId", String.valueOf(_data.GoodsAttributeId));
        afterAddShoppingCart(myRestClient.addShoppingCart(map), newQuantity);
    }

    @UiThread
    void afterAddShoppingCart(BaseModel bm, int newQuantity) {
        AndroidTool.dismissLoadDialog();
        if (bm == null) {
            AndroidTool.showToast(context, "商品添加失败");
            quantityView.setQuantity(_data.ProductCount);
        } else if (!bm.Successful) {
            AndroidTool.showToast(context, bm.Error);
            quantityView.setQuantity(_data.ProductCount);
        } else {
            _data.ProductCount++;
            changeItem(newQuantity);
        }
    }

    void changeItem(int newQuantity) {
        if (newQuantity == quantityView.getMinQuantity()) {
            quantityView.getChildAt(0).setEnabled(false);
            quantityView.getChildAt(2).setEnabled(true);
        } else if (newQuantity == quantityView.getMaxQuantity()) {
            quantityView.getChildAt(0).setEnabled(true);
            quantityView.getChildAt(2).setEnabled(false);
        } else {
            quantityView.getChildAt(0).setEnabled(true);
            quantityView.getChildAt(2).setEnabled(true);
        }
        //如果选中的的时候 才会通知adapter
        if (_data.isChecked) {
            baseRecyclerViewAdapter.itemNotify(true);
        }
    }

    @Override
    public void onLimitReached() {

    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
