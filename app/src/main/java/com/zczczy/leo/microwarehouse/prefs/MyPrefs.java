package com.zczczy.leo.microwarehouse.prefs;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Leo on 2015/3/9.
 */

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface MyPrefs {
    /**
     * D463CF459CE7AF242A727787E2DCDC8EC555869244E957647E08EDB14C9597C28CE9FA19437D1EA2
     *
     * @return
     */
    @DefaultString("")
    String token();

    @DefaultString("")
    String jPushAlias();

    @DefaultBoolean(false)
    boolean isSetAlias();

    @DefaultBoolean(true)
    boolean isFirst();

    @DefaultString("")
    String avatar();

    @DefaultInt(0)
    int verCode();

    @DefaultString("")
    String nickName();

    @DefaultLong(0L)
    long loginTimerCode();

    @DefaultLong(0L)
    long registerTimerCode();

    @DefaultString("")
    String userTypeStr();

    @DefaultString("")
    String userType();

    @DefaultBoolean(true)
    boolean isStartPush();

}
