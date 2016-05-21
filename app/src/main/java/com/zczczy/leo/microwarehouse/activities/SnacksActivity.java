package com.zczczy.leo.microwarehouse.activities;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zczczy on 2016/5/21.
 */
@EActivity(R.layout.activity_snacks)
public class SnacksActivity extends BaseActivity {

    @ViewById
    MyTitleBar myTitleBar;

    TextView textView;

    View view;

    Drawable title_search, title_search_scrolled;
    //
    @AfterViews
    void  afterView(){
        myTitleBar.getBackground().setAlpha(0);
        view = myTitleBar.getmCustomView();
        textView = (TextView) view.findViewById(R.id.txt_title_search);
        title_search = getResources().getDrawable(R.drawable.title_search);
        title_search_scrolled = getResources().getDrawable(R.drawable.title_search_scrolled);
        title_search.setBounds(0, 0, title_search.getMinimumWidth(), title_search.getMinimumHeight());
        title_search_scrolled.setBounds(0, 0, title_search_scrolled.getMinimumWidth(), title_search_scrolled.getMinimumHeight());

    }
}
