package com.zczczy.leo.microwarehouse.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cleveroad.slidingtutorial.PageFragment;
import com.cleveroad.slidingtutorial.SimplePagerFragment;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.activities.WelcomeActivity_;

import org.androidannotations.annotations.EFragment;

/**
 * @author Created by LuLeo on 2016/6/14.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/14.
 */
@EFragment
public class CustomPresentationPagerFragment extends SimplePagerFragment {


    @Override
    protected int getPagesCount() {
        return 5;
    }

    @Override
    protected PageFragment getPage(int position) {
        position %= 5;
        int layoutResId = R.drawable.index_one;

        switch (position) {
            case 0:
                layoutResId = R.drawable.index_one;
                break;
            case 1:
                layoutResId = R.drawable.index_two;
                break;
            case 2:
                layoutResId = R.drawable.index_three;
                break;
            case 3:
                layoutResId = R.drawable.index_four;
                break;
            case 4:
                layoutResId = R.drawable.index_five;
                break;
            default:

                break;
        }
        return FirstIndexPageFragment_.builder().layoutResId(layoutResId).build();
    }

    @Override
    protected int getPageColor(int position) {
        return Color.TRANSPARENT;
    }

    @Override
    protected boolean isInfiniteScrollEnabled() {
        return false;
    }

    @Override
    protected boolean onSkipButtonClicked(View skipButton) {
        getActivity().finish();
        WelcomeActivity_.intent(this).start();
        return true;
    }

    @Override
    public int getButtonSkipResId() {
        // id of skip button
        return R.id.tvSkip;
    }

    @Override
    public int getViewPagerResId() {
        // id of view pager
        return R.id.viewPager;
    }

    @Override
    public int getIndicatorResId() {
        // id of circular indicator
        return R.id.indicator;
    }

    /**
     * When last position will be reached then remove fragment from the screen.
     */
    @Override
    public void onPageSelected(int position) {
        if (!isInfiniteScrollEnabled() && position == getPagesCount()) {
            getActivity().finish();
            WelcomeActivity_.intent(this).start();
        }
        if (position != 4) {
            getSkipButton().setVisibility(View.INVISIBLE);
        } else {
            getSkipButton().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSkipButton().setVisibility(View.INVISIBLE);
    }

}
