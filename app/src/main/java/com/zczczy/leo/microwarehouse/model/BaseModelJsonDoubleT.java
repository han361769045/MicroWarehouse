package com.zczczy.leo.microwarehouse.model;

import java.io.Serializable;

/**
 * Created by zhangyan on 2016/10/13.
 * 双泛型bean
 */

public class BaseModelJsonDoubleT<T1,T2> extends BaseModel implements Serializable {

    public T1 Data;

    public T2 Data2;

}