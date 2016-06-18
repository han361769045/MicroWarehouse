package com.zczczy.leo.microwarehouse.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.fragments.MemberTaskOrderFragment;
import com.zczczy.leo.microwarehouse.fragments.MemberTaskOrderFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author Created by LuLeo on 2016/6/17.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/17.
 */
@EActivity(R.layout.activity_member_task_order)
public class MemberTaskOrderActivity extends BaseActivity {

    @ViewById
    RadioButton rb_publish, rb_receiver;

    FragmentManager fragmentManager;

    MemberTaskOrderFragment memberPublishTaskOrderFragment, memberReceiverTaskOrderFragment;

    @AfterViews
    void afterView() {
        fragmentManager = getSupportFragmentManager();
        changeFragment("0");
    }

    @CheckedChange
    void rb_publish(boolean isChecked) {
        changeFragment(isChecked ? "0" : "1");
    }

    void changeFragment(String parameter) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (memberPublishTaskOrderFragment != null) {
            transaction.hide(memberPublishTaskOrderFragment);
        }
        if (memberReceiverTaskOrderFragment != null) {
            transaction.hide(memberReceiverTaskOrderFragment);
        }
        if (rb_publish.isChecked()) {
            if (memberPublishTaskOrderFragment == null) {
                memberPublishTaskOrderFragment = MemberTaskOrderFragment_.builder().type(parameter).build();
                transaction.add(R.id.task_fragment, memberPublishTaskOrderFragment);
            } else {
                transaction.show(memberPublishTaskOrderFragment);
            }
        } else {
            if (memberReceiverTaskOrderFragment == null) {
                memberReceiverTaskOrderFragment = MemberTaskOrderFragment_.builder().type(parameter).build();
                transaction.add(R.id.task_fragment, memberReceiverTaskOrderFragment);
            } else {
                transaction.show(memberReceiverTaskOrderFragment);
            }
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
