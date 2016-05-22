package com.zczczy.leo.microwarehouse.prefs;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Leo on 2015/3/9.
 */

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface MyPrefs {
    /**
     * @return
     */
    @DefaultString("")
    String token();

    @DefaultString("")
    String avatar();

    @DefaultString("")
    String nickName();
}
