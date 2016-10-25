package com.zczczy.leo.microwarehouse.viewgroup;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

/**
 * Created by LeoLu on 2016/10/25.
 */

public class MyTitleBarBehavior extends CoordinatorLayout.Behavior<ViewGroup> {


    private float targetY = -1;

    TranslateAnimation tAnim = new TranslateAnimation(1080, 0, 0, 0);//横向位移400个单

    public MyTitleBarBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ViewGroup child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewGroup child, View dependency) {
        float scaleY = Math.abs(dependency.getY()) / dependency.getHeight();
        if (scaleY == 0) {
            child.setVisibility(View.GONE);
        } else {
            child.setVisibility(View.VISIBLE);
            if ((int) Math.abs(dependency.getY()) < 50) {
                tAnim.setDuration(500);
                child.getChildAt(0).startAnimation(tAnim);
            }
        }
        child.setAlpha(scaleY);
        return true;
    }


//    @Override
//    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, ViewGroup child,
//                                       View directTargetChild, View target, int nestedScrollAxes) {
//        if (targetY == -1) {
//            targetY = target.getY();
//        }
//        Log.e("onStartNestedScroll", "targetY-->>" + targetY);
//        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
//    }
//
//    @Override
//    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, ViewGroup child, View target,
//                                  int dx, int dy, int[] consumed) {
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
//        float scrooY = targetY - Math.abs(target.getY());
//        if (scrooY == 0) {
//            child.setVisibility(View.GONE);
//        } else {
//            child.setVisibility(View.VISIBLE);
//        }
//        Log.e("onNestedPreScroll", "target.getY()-->>" + target.getY());
//        float scaleY = scrooY / targetY;
//        Log.e("onNestedPreScroll", "targetY-->>" + targetY);
//        Log.e("onNestedPreScroll", "scrooY-->>" + scrooY);
//        Log.e("onNestedPreScroll", "scaleY-->>" + scaleY);
//        child.setAlpha(scaleY);
////        child.setTranslationY(child.getHeight() * scaleY);
//    }

}
