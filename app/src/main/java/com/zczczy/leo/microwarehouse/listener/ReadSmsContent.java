package com.zczczy.leo.microwarehouse.listener;

import android.app.Activity;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leo on 2016/5/22.
 */
public class ReadSmsContent extends ContentObserver {
    private Cursor cursor;
    private Activity mActivity;
    private EditText mEditText;

    public ReadSmsContent(Handler handler, Activity activity, EditText editText) {
        super(handler);
        this.mActivity = activity;
        this.mEditText = editText;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.e("tag", "onChange");
        // 读取收件箱中指定号码的短信
//        cursor = mActivity.getContentResolver().query(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "body", "read"}, null, null, "_id desc");
        CursorLoader cursorLoader = new CursorLoader(mActivity, Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "body", "read"}, null, null, "_id desc");

//        CursorLoader cursorLoader = new CursorLoader(mActivity,
//        Uri.parse("content://sms/inbox"),
//        new String[]{"_id", "address", "body", "read"},
//        "address=? and read=?",
//        new String[]{"5554", "0"}, "_id desc");
        cursor = cursorLoader.loadInBackground();
        if (cursor != null && cursor.getCount() > 0) {
            ContentValues values = new ContentValues();
            values.put("read", "1");        //修改短信为已读模式
            cursor.moveToNext();
            //获取内容
            int smsbodyColumn = cursor.getColumnIndex("body");
            String smsBody = cursor.getString(smsbodyColumn);
            Log.e("399", "smsBody :" + smsBody);
            //获取是哪个号码发过来的
            int address = cursor.getColumnIndex("address");
            String str = cursor.getString(address);

            String verifyCode = getDynamicPassword(smsBody);
            if (TextUtils.isEmpty(verifyCode)) {
                return;
            }
            if (mEditText == null) {
                throw new RuntimeException("你传的EditText为空");
            }
            mEditText.setText(verifyCode);
            //EditText获取焦点，3个属性必须同时设置
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.requestFocus();
            mEditText.setSelection(verifyCode.length());//设置光标位置

            //在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上，         会发生崩溃
            if (Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
        }
    }

    /**
     * 从字符串中截取连续4位数字
     * 用于从短信中获取动态密码
     *
     * @param str 短信内容
     * @return 截取得到的4位动态密码
     */
    public String getDynamicPassword(String str) {
        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while (m.find()) {
            if (m.group().length() == 4) {
                dynamicPassword = m.group();
            }
        }
        return dynamicPassword;
    }
}
