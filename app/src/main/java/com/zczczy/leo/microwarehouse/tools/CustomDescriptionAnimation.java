package com.zczczy.leo.microwarehouse.tools;

import android.view.View;

import com.daimajia.slider.library.Animations.BaseAnimationInterface;

/**
 * @author Created by LuLeo on 2016/6/13.
 *         you can contact me at :361769045@qq.com
 * @since 2016/6/13.
 */
public class CustomDescriptionAnimation implements BaseAnimationInterface {
    @Override
    public void onPrepareCurrentItemLeaveScreen(View current) {
        View descriptionLayout = current.findViewById(com.daimajia.slider.library.R.id.description_layout);
        if (descriptionLayout != null) {
            current.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPrepareNextItemShowInScreen(View next) {
        View descriptionLayout = next.findViewById(com.daimajia.slider.library.R.id.description_layout);
        if (descriptionLayout != null) {
            next.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCurrentItemDisappear(View view) {
        View descriptionLayout = view.findViewById(com.daimajia.slider.library.R.id.description_layout);
        if (descriptionLayout != null) {
            view.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNextItemAppear(View view) {
        View descriptionLayout = view.findViewById(com.daimajia.slider.library.R.id.description_layout);
        if (descriptionLayout != null) {
            view.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.INVISIBLE);

        }
    }
}
