package com.zczczy.leo.microwarehouse.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zczczy.leo.microwarehouse.R;
import com.zczczy.leo.microwarehouse.model.BaseModelJson;
import com.zczczy.leo.microwarehouse.model.MemberInfoModel;
import com.zczczy.leo.microwarehouse.rest.MyErrorHandler;
import com.zczczy.leo.microwarehouse.rest.MyRestClient;
import com.zczczy.leo.microwarehouse.tools.AndroidTool;
import com.zczczy.leo.microwarehouse.tools.Constants;
import com.zczczy.leo.microwarehouse.tools.ImageUtil;
import com.zczczy.leo.microwarehouse.viewgroup.MyTitleBar;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by Leo on 2016/5/25.
 */
@EActivity(R.layout.activity_account_management)
public class AccountManagementActivity extends BaseActivity {

    @ViewById
    MyTitleBar myTitleBar;

    @ViewById
    ImageView avatar;

    @ViewById
    RelativeLayout rl_avatar, rl_real_name, rl_qq, rl_blog, rl_email, rl_safe, rl_xie_yi, rl_mian_ze;

    @ViewById
    TextView txt_real_name, txt_qq, txt_blog, txt_email;

    @StringRes
    String text_zhu_ce, text_mian_ze;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;

    MemberInfoModel memberInfoModel;

    String image;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
        myRestClient.setHeader("Token", pre.token().get());
        myRestClient.setHeader("Kbn", Constants.ANDROID);
    }

    @AfterViews
    void afterView() {
        AndroidTool.showLoadDialog(this);
        memberInfoModel = new MemberInfoModel();
        getMemberInfo();
    }

    @Click
    void rl_avatar() {

        getPermissions();
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 127);
            } else {
                takePhoto();
            }
        } else {
            takePhoto();
        }
    }

    void takePhoto() {
        PhotoPickerIntent intent = new PhotoPickerIntent(AccountManagementActivity.this);
        intent.setPhotoCount(1);
        intent.setShowCamera(true);
        intent.setShowGif(false);
        startActivityForResult(intent, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                AndroidTool.showToast(this, "您拒绝授权，该功能不可用");
                return;
            }
        }
        takePhoto();
    }


    /**
     * 上传图片
     *
     * @param avatarUrl
     */
    @Background
    void uploadAvatar(String avatarUrl) {
        FileSystemResource image = new FileSystemResource(avatarUrl);
        afterUploadAvatar(myRestClient.uploadAvatar(image));
    }

    /**
     * 上传成功后 上传地址
     *
     * @param bmj
     */
    @UiThread
    void afterUploadAvatar(String bmj) {
        if (bmj == null) {
            AndroidTool.dismissLoadDialog();
            AndroidTool.showToast(this, no_net);
        } else if (bmj.equals("0")) {
            AndroidTool.dismissLoadDialog();
            AndroidTool.showToast(this, "上传失败");
        } else {
            Log.e("img", bmj);
            updateMemberInfoImg(bmj);
        }
    }

    /**
     * 更新头像图片地址
     *
     * @param img
     */
    @Background
    void updateMemberInfoImg(String img) {
        Map<String, String> map = new HashMap<>(1);
        map.put("HeadImg", img);
        afterUpdateMemberInfoImg(myRestClient.updateMemberInfoImg(map));
    }

    @UiThread
    void afterUpdateMemberInfoImg(BaseModelJson<String> bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!bmj.Successful) {
            AndroidTool.showToast(this, "上传失败");
        } else {
            pre.avatar().put(bmj.Data);
            Picasso.with(this).load(bmj.Data).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(avatar);
        }
    }

    /**
     * 选择图片
     *
     * @param resultCode 请求code
     * @param photos     返回
     */
    @OnActivityResult(1000)
    void onSelectPicture(int resultCode, @OnActivityResult.Extra(value = PhotoPickerActivity.KEY_SELECTED_PHOTOS) ArrayList<String> photos) {
        if (resultCode == RESULT_OK) {
            image = photos.get(0);
            ImageUtil.corpIntent(this, AndroidTool.getUri(photos.get(0)), AndroidTool.getUri(photos.get(0)));
        }
    }

    @OnActivityResult(ImageUtil.IMAGE_CROP)
    void onCodeResult() {
        AndroidTool.showLoadDialog(this);
        ImageUtil.resetPhotp(image);
        uploadAvatar(image);
    }


    @Click
    void rl_real_name() {
        ChangeInfoActivity_.intent(this).flagT("1").memberInfoModel(memberInfoModel).content(txt_real_name.getText().toString()).startForResult(1000);
    }

    @Click
    void rl_qq() {
        ChangeInfoActivity_.intent(this).flagT("2").memberInfoModel(memberInfoModel).content(txt_qq.getText().toString()).startForResult(1000);
    }

    @Click
    void rl_blog() {
        ChangeInfoActivity_.intent(this).flagT("3").memberInfoModel(memberInfoModel).content(txt_blog.getText().toString()).startForResult(1000);
    }

    @Click
    void rl_email() {
        ChangeInfoActivity_.intent(this).flagT("4").memberInfoModel(memberInfoModel).content(txt_email.getText().toString()).startForResult(1000);
    }


    @OnActivityResult(1000)
    void onResult(int resultCode, @OnActivityResult.Extra String contents) {
        switch (resultCode) {
            case 1000:
                txt_real_name.setText(contents);
                memberInfoModel.MemberRealName = contents;
                break;
            case 1001:
                txt_qq.setText(contents);
                memberInfoModel.MemberQQ = contents;
                break;
            case 1002:
                txt_blog.setText(contents);
                memberInfoModel.MemberBlog = contents;
                break;
            case 1003:
                txt_email.setText(contents);
                memberInfoModel.MemberEmail = contents;
                break;
        }
    }


    //获取会员信息
    @Background
    void getMemberInfo() {
        afterGetMemberInfo(myRestClient.getMemberInfo());
    }

    @UiThread
    void afterGetMemberInfo(BaseModelJson<MemberInfoModel> bmj) {
        AndroidTool.dismissLoadDialog();
        if (bmj == null) {
            AndroidTool.showToast(this, no_net);
        } else if (!bmj.Successful) {
            AndroidTool.showToast(this, bmj.Error);
        } else {
            txt_real_name.setText(bmj.Data.MemberRealName);
            txt_email.setText(bmj.Data.MemberEmail);
            txt_qq.setText(bmj.Data.MemberQQ);
            txt_blog.setText(bmj.Data.MemberBlog);
            if (!StringUtils.isEmpty(bmj.Data.HeadImg)) {
                Picasso.with(this).load(bmj.Data.HeadImg).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(avatar);
            }
            memberInfoModel = bmj.Data;
            pre.userTypeStr().put(memberInfoModel.UserTypeStr);
        }
    }


    @Click
    void rl_safe() {
        ChangePwdActivity_.intent(this).memberInfoModel(memberInfoModel).startForResult(1001);
    }

    @OnActivityResult(1001)
    void setPassword(int resultCode) {
        if (resultCode == 1000) {
            pre.clear();
            finish();
            LoginActivity_.intent(this).start();
        } else if (resultCode == 1001) {
            memberInfoModel.UserPw = "aaa";
        }
    }

    @Click
    void rl_xie_yi() {
        CommonWebViewActivity_.intent(this).linkUrl(Constants.ROOT_URL + "RegisterProtocol").title(text_zhu_ce).start();
    }

    @Click
    void rl_mian_ze() {
        CommonWebViewActivity_.intent(this).linkUrl(Constants.ROOT_URL + "Disclaimer").title(text_mian_ze).start();
    }

    @Click
    void btn_exit() {
        if (checkUserIsLogin()) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("提示").setMessage("确定要注销吗？").setPositiveButton("注销", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pre.clear();
                    finish();

                }
            }).setNegativeButton("取消", null).setIcon(R.mipmap.ic_launcher).create().show();
        }
    }

}
