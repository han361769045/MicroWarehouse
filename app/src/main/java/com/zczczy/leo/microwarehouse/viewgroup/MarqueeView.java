package com.zczczy.leo.microwarehouse.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.zczczy.leo.microwarehouse.model.GoodsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zczczy on 2016/11/17.
 */

public class MarqueeView extends ViewFlipper {

    private Context mContext;
    private List<String> notices;
    private boolean isSetAnimDuration = false;

    private int interval = 2000;
    private int animDuration = 500;
    private int textSize = 14;
    private int textColor = 0xffffffff;

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if (notices == null) {
            notices = new ArrayList<>();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle, defStyleAttr, 0);
        interval = typedArray.getInteger(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = typedArray.hasValue(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvAnimDuration);
        animDuration = typedArray.getInteger(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration);
        if (typedArray.hasValue(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvTextSize)) {
            textSize = (int) typedArray.getDimension(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvTextSize, textSize);
            textSize = DisplayUtil.px2sp(mContext, textSize);
        }
        textColor = typedArray.getColor(com.sunfusheng.marqueeview.R.styleable.MarqueeViewStyle_mvTextColor, textColor);
        typedArray.recycle();

        setFlipInterval(interval);

        Animation animIn = AnimationUtils.loadAnimation(mContext, com.sunfusheng.marqueeview.R.anim.anim_marquee_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(mContext, com.sunfusheng.marqueeview.R.anim.anim_marquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    // 根据公告字符串启动轮播
    public void startWithText(final String notice) {
        if (TextUtils.isEmpty(notice)) return;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startWithFixedWidth(notice, getWidth());
            }
        });
    }

    List<GoodsModel> goodsModels;

    // 根据公告字符串列表启动轮播
    public void startWithList(List<String> notices) {
        setNotices(notices);
        start();
    }

    // 根据宽度和公告字符串启动轮播
    private void startWithFixedWidth(String notice, int width) {
        int noticeLength = notice.length();
        int dpW = DisplayUtil.px2dip(mContext, width);
        int limit = dpW/textSize;
        if (dpW == 0) {
            throw new RuntimeException("Please set MarqueeView width !");
        }

        if (noticeLength <= limit) {
            notices.add(notice);
        } else {
            int size = noticeLength/limit + (noticeLength%limit != 0? 1:0);
            for (int i=0; i<size; i++) {
                int startIndex = i*limit;
                int endIndex = ((i+1)*limit >= noticeLength? noticeLength:(i+1)*limit);
                notices.add(notice.substring(startIndex, endIndex));
            }
        }
        start();
    }

    // 启动轮播
    public boolean start() {
        if (notices == null || notices.size() == 0) return false;
        removeAllViews();
        for (String notice:notices) {
            addView(createTextView(notice));
        }
        if (notices.size() > 1) {
            startFlipping();
        }
        return true;
    }

    // 创建ViewFlipper下的TextView
    private TextView createTextView(String text) {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        SpannableString str = new SpannableString(text);
        str.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        tv.setText(str);
        tv.setTextColor(textColor);
        tv.setTextSize(textSize);
        return tv;
    }

    public List<String> getNotices() {
        return notices;
    }

    //列表插入数据
    public void setNotices(List<String> notices) {
        this.notices = notices;
    }

}