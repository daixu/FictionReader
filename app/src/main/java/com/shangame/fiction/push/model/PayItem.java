package com.shangame.fiction.push.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by Speedy on 2018/9/18
 */
public class PayItem implements Parcelable {
    public int goldtype;
    public int number;
    public int propid;

    protected PayItem(Parcel in) {
        goldtype = in.readInt();
        number = in.readInt();
        propid = in.readInt();
    }

    public static final Creator<PayItem> CREATOR = new Creator<PayItem>() {
        @Override
        public PayItem createFromParcel(Parcel in) {
            return new PayItem(in);
        }

        @Override
        public PayItem[] newArray(int size) {
            return new PayItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(goldtype);
        dest.writeInt(number);
        dest.writeInt(propid);
    }

    @Override
    public String toString() {
        return "PayItem{" +
                "goldtype=" + goldtype +
                ", number=" + number +
                ", propid=" + propid +
                '}';
    }
}
