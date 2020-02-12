package com.shangame.fiction.push.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Create by Speedy on 2018/9/18
 */
public class PayModel implements Parcelable {

    public int price;
    public long readmoney;
    public List<PayItem> currdata;


    protected PayModel(Parcel in) {
        price = in.readInt();
        readmoney = in.readLong();
        currdata = in.createTypedArrayList(PayItem.CREATOR);
    }

    public static final Creator<PayModel> CREATOR = new Creator<PayModel>() {
        @Override
        public PayModel createFromParcel(Parcel in) {
            return new PayModel(in);
        }

        @Override
        public PayModel[] newArray(int size) {
            return new PayModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(price);
        dest.writeLong(readmoney);
        dest.writeTypedList(currdata);
    }

    @Override
    public String toString() {
        return "PayModel{" +
                "price=" + price +
                "readmoney=" + readmoney +
                ", currdata=" + currdata +
                '}';
    }
}
