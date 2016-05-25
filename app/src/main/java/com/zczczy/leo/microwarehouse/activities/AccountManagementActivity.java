package com.zczczy.leo.microwarehouse.activities;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.MemberInfo;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * Created by zczczy on 2016/5/21.
 *  账户管理
 */
@EActivity(R.layout.activity_account_managerment)
public class AccountManagementActivity extends  BaseActivity {

    @ViewById
    EditText edt_realname,edt_qq,edt_blog,edt_email;
    @RestService
    MyRestClient myRestClient;

    @ViewById
    Button btn_save,btn_exit;
    @ViewById
    ImageView img_avatar;



    @Bean
    MyErrorHandler myErrorHandler;

    @AfterInject
    void aftetInject(){
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView(){
        getMemberInfo();
    }

    //获取会员信息
    @Background
    void getMemberInfo() {
        myRestClient.setHeader("Token", "D463CF459CE7AF242A727787E2DCDC8EC555869244E957647E08EDB14C9597C28CE9FA19437D1EA2");
        myRestClient.setHeader("Kbn", "1");
        afterGetMemberInfo(myRestClient.GetMemberInfo());
    }

    @UiThread
    void afterGetMemberInfo(BaseModelJson<MemberInfo> bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!bmj.Successful) {
            AndroidTool.showToast(this, bmj.Error);
        } else {
            edt_realname.setText(bmj.Data.MemberRealName);
            edt_email.setText(bmj.Data.MemberEmail);
            edt_qq.setText(bmj.Data.MemberQQ);
            edt_blog.setText(bmj.Data.MemberBlog);
            if (!StringUtils.isEmpty(bmj.Data.HeadImg)) {
                Picasso.with(this).load(bmj.Data.HeadImg).placeholder(R.drawable.default_header).error(R.drawable.default_header).into(img_avatar);
            }
        }
    }



    @Click
    void btn_save(){

        changeInfo(edt_realname.getText().toString().trim(),edt_qq.getText().toString().trim(),edt_blog.getText().toString().trim(),
                edt_email.getText().toString().trim());
    }



    @Background
    void changeInfo(String MemberRealName,String MemberQQ,String MemberBlog,String MemberEmail){
        myRestClient.setHeader("Token","D463CF459CE7AF242A727787E2DCDC8EC555869244E957647E08EDB14C9597C28CE9FA19437D1EA2");
        myRestClient.setHeader("Kbn","1");
        HashMap map =new HashMap();
        map.put("MemberRealName",MemberRealName);
        map.put("MemberQQ",MemberQQ);
        map.put("MemberBlog",MemberBlog);
        map.put("MemberEmail",MemberEmail);
        BaseModelJson<String>bmj=myRestClient.PerfectMemberInfo(map);
        aftersetInfo(bmj);

    }

    @UiThread
    void aftersetInfo(BaseModelJson<String>bmj){
        if(bmj==null){
            AndroidTool.showToast(this,"网络连接失败");

        }
        else if (bmj.Successful){
            AndroidTool.showToast(this,"保存成功");
            finish();

        }
        else {
            AndroidTool.showToast(this,bmj.Error);
        }
    }

}
