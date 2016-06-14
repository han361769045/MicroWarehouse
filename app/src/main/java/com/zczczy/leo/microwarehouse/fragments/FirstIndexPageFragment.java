package com.zczczy.leo.microwarehouse.fragments;

import android.widget.ImageView;

import com.cleveroad.slidingtutorial.PageFragment;
import com.cleveroad.slidingtutorial.TransformItem;
import com.zczczy.leo.microwarehouse.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * @author Created by LuLeo on 2016/6/14.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/14.
 */
@EFragment
public class FirstIndexPageFragment extends PageFragment {


    @FragmentArg
    int layoutResId;

    @ViewById
    ImageView rootFirstPage;

    @Override
    protected TransformItem[] provideTransformItems() {
        return new TransformItem[]{
                new TransformItem(R.id.rootFirstPage, true, 20),
        };
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_first_index_page;
    }

    @AfterViews
    void afterView() {
        rootFirstPage.setBackgroundResource(layoutResId);
    }

}
