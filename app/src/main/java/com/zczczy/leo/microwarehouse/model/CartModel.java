package com.zczczy.leo.microwarehouse.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Leo on 2016/5/20.
 */
public class CartModel implements Parcelable {

    public boolean isChecked;


    public CartModel() {

    }

    protected CartModel(Parcel in) {
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
