package com.zczczy.leo.microwarehouse.activities;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;

import com.squareup.picasso.Picasso;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModel;
import com.zczczy.leo.microwarehouse.model.OrderDetailModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * Created by leo on 2016/5/8.
 */
@EActivity(R.layout.activity_publish_review)
public class PublishReviewActivity extends BaseActivity {

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    @ViewById
    ImageView img_store_pic;

    @ViewById
    EditText edt_review_content;

    @ViewById
    RatingBar ratingBar;

    @ViewById
    RadioButton rb_good_review, rb_soso_review, rb_bad_review;

    @Extra
    OrderDetailModel model;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        if (!StringUtils.isEmpty(model.GoodsImgSl)) {
            Picasso.with(this).load(model.GoodsImgSl).error(R.drawable.goods_default).placeholder(R.drawable.goods_default).into(img_store_pic);
        }
    }

    @Click
    void btn_comment() {
        if (ratingBar.getRating() <= 0) {
            AndroidTool.showToast(this, "请选择星级");
        } else if (AndroidTool.checkIsNull(edt_review_content)) {
            AndroidTool.showToast(this, "请输入评论内容");
        } else {
            AndroidTool.showLoadDialog(this);
            int dj;
            if (rb_good_review.isChecked()) {
                dj = 1;
            } else if (rb_soso_review.isChecked()) {
                dj = 2;
            } else {
                dj = 3;
            }
            publishReview(dj);
        }
    }

    @Background
    void publishReview(int dj) {
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
        HashMap<String, Object> map = new HashMap<>();
        String GoodsInfoId = model.GoodsInfoId;
        String MOrderDetailId = model.MOrderDetailId;
        map.put("XNum", (int) ratingBar.getRating());
        map.put("GoodsInfoId", GoodsInfoId);
        map.put("MOrderDetailId", MOrderDetailId);
        map.put("GoodsCommentsDj", dj);
        map.put("GoodsCommentsNr", edt_review_content.getText().toString().trim());
        afterPublishReview(myRestClient.insertGoodsComments(map));
    }


    @UiThread
    void afterPublishReview(BaseModel bm) {
        AndroidTool.dismissLoadDialog();
        if (bm == null) {
            AndroidTool.showToast(this, no_net);
        } else if (bm.Successful) {
            AndroidTool.showToast(this, "评论成功");
            setResult(RESULT_OK);
            finish();
        } else {
            AndroidTool.showToast(this, bm.Error);
        }
    }
}
