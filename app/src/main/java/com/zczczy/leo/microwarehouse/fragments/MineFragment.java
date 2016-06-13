package com.zczczy.leo.microwarehouse.fragments;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.AccountManagementActivity_;
import com.zczczy.leo.microwarehouse.activities.ApplyDealerActivity_;
import com.zczczy.leo.microwarehouse.activities.CommonWebViewActivity_;
import com.zczczy.leo.microwarehouse.activities.FeedbackActivity_;
import com.zczczy.leo.microwarehouse.activities.LoginActivity_;
import com.zczczy.leo.microwarehouse.activities.MemberOrderActivity_;
import com.zczczy.leo.microwarehouse.activities.ReviewActivity_;
import com.zczczy.leo.microwarehouse.activities.ShippingAddressActivity_;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.MemberInfoModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

/**
 * Created by Leo on 2016/5/20.
 */
@EFragment(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {

    @ViewById
    ImageView avatar;

    @ViewById
    TextView txt_username, txt_user_type;

    @StringRes
    String text_username, text_no_pay_order, text_take_goods_order, text_order, text_about_us;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
    }

    @AfterViews
    void afterView() {
        if (checkUserIsLogin()) {
            getMemberInfo();
        }
    }

    void setData() {
        if (checkUserIsLogin()) {
            if (!StringUtils.isEmpty(pre.avatar().get())) {
                Picasso.with(getActivity()).load(pre.avatar().get()).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(avatar);
            }
            txt_username.setText(pre.nickName().get());
            txt_user_type.setText(pre.userTypeStr().get());
        } else {
            txt_username.setText(text_username);
            txt_user_type.setText(pre.userTypeStr().get());
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

    //获取会员信息
    @Background
    void getMemberInfo() {
        afterGetMemberInfo(myRestClient.getMemberInfo());
    }

    @UiThread
    void afterGetMemberInfo(BaseModelJson<MemberInfoModel> bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!bmj.Successful) {
            AndroidTool.showToast(this, bmj.Error);
        } else {
            pre.userTypeStr().put(bmj.Data.UserTypeStr);
            pre.avatar().put(bmj.Data.HeadImg);
            txt_user_type.setText(pre.userTypeStr().get());
            pre.userType().put(bmj.Data.UserType);
        }
    }

    @Click
    void txt_manager() {
        if (checkUserIsLogin()) {
            AccountManagementActivity_.intent(this).start();
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_coupon() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "敬请期待");
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_my_review() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "敬请期待");
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_bookmark() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "敬请期待");
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void rl_all_order() {
        if (checkUserIsLogin()) {
            MemberOrderActivity_.intent(this).orderState(2).title("全部").start();
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_hole_order() {
        if (checkUserIsLogin()) {
            MemberOrderActivity_.intent(this).orderState(0).title(text_no_pay_order).start();
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_waiting_order() {
        if (checkUserIsLogin()) {
            MemberOrderActivity_.intent(this).orderState(1).title(text_take_goods_order).start();
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void txt_review() {
        if (checkUserIsLogin()) {
            ReviewActivity_.intent(this).start();
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
        CommonWebViewActivity_.intent(this).title(text_about_us).linkUrl(Constants.ROOT_URL + "Index").start();
    }

    @Click
    void rl_feedback() {
        if (checkUserIsLogin()) {
            FeedbackActivity_.intent(this).start();
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void rl_apply() {
        if (checkUserIsLogin()) {
            ApplyDealerActivity_.intent(this).start();
        } else {
            LoginActivity_.intent(this).start();
        }

    }

    @Click
    void rl_share() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "敬请期待");
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    @Click
    void rl_scan() {
        if (checkUserIsLogin()) {
            AndroidTool.showToast(this, "敬请期待");
        } else {
            LoginActivity_.intent(this).start();
        }
    }
}
