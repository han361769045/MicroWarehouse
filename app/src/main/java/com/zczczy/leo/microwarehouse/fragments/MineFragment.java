package com.zczczy.leo.microwarehouse.fragments;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.LoginActivity_;
import com.zczczy.leo.microwarehouse.activities.ShippingAddressActivity_;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.springframework.util.StringUtils;

/**
 * Created by Leo on 2016/5/20.
 */
@EFragment(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {

    @ViewById
    ImageView avatar;

    @ViewById
    TextView txt_username;

    @StringRes
    String text_username;

    @AfterViews
    void afterView() {

    }

    void setData() {
        if (checkUserIsLogin()) {
            if (!StringUtils.isEmpty(pre.avatar().get())) {
                Picasso.with(getActivity()).load(pre.avatar().get()).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(avatar);
            }
            txt_username.setText(pre.nickName().get());
        } else {
            txt_username.setText(text_username);
            avatar.setImageResource(R.drawable.default_avatar);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setData();
        }
    }


    @Click
    void txt_manager() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "开发中");

        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_coupon() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "开发中");
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_my_review() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "开发中");
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_bookmark() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "开发中");
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void rl_all_order() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "开发中");
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_hole_order() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "开发中");
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_waiting_order() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "开发中");
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_review() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "开发中");
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void rl_my_address() {
        if (checkUserIsLogin()) {
            ShippingAddressActivity_.intent(this).start();
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void rl_about_us() {

    }

    @Click
    void rl_share() {

    }

    @Click
    void rl_scan() {

    }


}
