package com.zczczy.leo.microwarehouse.activities;

import android.view.MotionEvent;
import android.view.View;

import android.widget.RelativeLayout;

import com.zczczy.leo.microwarehouse.R;

import com.zczczy.leo.microwarehouse.model.AdvertModel;

import com.zczczy.leo.microwarehouse.model.BaseModelJson;

import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.views.LoopViewPager;
import com.zczczy.leo.microwarehouse.views.ScalePagerTransformer;
import com.zczczy.leo.microwarehouse.views.WelfareAdapter;


import org.androidannotations.annotations.AfterViews;

import org.androidannotations.annotations.Background;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;


import java.util.ArrayList;
import java.util.List;



/**
 * Created by zczczy on 2016/9/30.
 * vierpager
 */
@EActivity(R.layout.testvipcard_layout)
public class TestActivity extends BaseActivity {


    private WelfareAdapter adapter;

    @RestService
    MyRestClient myRestClient;

    @ViewById
    RelativeLayout activity_test;

    @ViewById
    LoopViewPager banner_Vp;


    @AfterViews
    void afterView() {
        getAdvert();
        banner_Vp.setOffscreenPageLimit(3);
        banner_Vp.setPageTransformer(true, new ScalePagerTransformer());
        //设置Pager之间的间距
        banner_Vp.setPageMargin(30);
        //这里要把父类的touch事件传给子类，不然边上的会滑不动
        activity_test.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return banner_Vp.dispatchTouchEvent(event);
            }
        });

    }

   @Background
    void getAdvert(){
       BaseModelJson bmj=myRestClient.GetHomeAdvertBanner("16");
       afterGetAdvert(bmj);
   }
  @UiThread
    void afterGetAdvert(BaseModelJson<List<AdvertModel>>bmj){
      if (bmj!=null&&bmj.Successful){
          adapter = new WelfareAdapter(this);
          adapter.addDatas(bmj.Data);
          banner_Vp.setAdapter(adapter);
          AndroidTool.showToast(this,"12123132131");
      }
  }

}







