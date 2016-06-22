package com.zczczy.leo.microwarehouse.activities;

import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FromHtml;
import org.androidannotations.annotations.ViewById;

/**
 * @author Created by LuLeo on 2016/6/21.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/21.
 */
@EActivity(R.layout.activity_washing)
public class WashingActivity extends BaseActivity {

    @ViewById
    @FromHtml(R.string.text_washing)
    TextView txt_washing;



}
