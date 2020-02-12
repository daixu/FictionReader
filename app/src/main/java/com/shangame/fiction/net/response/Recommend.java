package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Create by Speedy on 2019/8/7
 */
public class Recommend implements Parcelable {

    public File file;
    public boolean isClick;

    public Recommend() {}

    protected Recommend(Parcel in) {
        isClick = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isClick ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recommend> CREATOR = new Creator<Recommend>() {
        @Override
        public Recommend createFromParcel(Parcel in) {
            return new Recommend(in);
        }

        @Override
        public Recommend[] newArray(int size) {
            return new Recommend[size];
        }
    };
}
