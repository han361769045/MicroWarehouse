package com.zczczy.leo.microwarehouse.fragments;



import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.LoginActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * Created by Leo on 2016/5/20.
 */
@EFragment(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {

    @ViewById
    TextView txt_login;

    @Click
    void txt_login(){

        LoginActivity_.intent(this).start();
    }




}
