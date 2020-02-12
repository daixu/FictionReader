package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Create by Speedy on 2019/7/30
 */
public class TimeConfigResponse implements Parcelable {
    public List<TimeDataBean> timedata;

    public static class TimeDataBean implements Parcelable {
        /**
         * years : 1
         * times : 2
         * timetext : sample string 3
         */

        public int years;
        public int times;
        public String timetext;

        public boolean isClick = false;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.years);
            dest.writeInt(this.times);
            dest.writeString(this.timetext);
            dest.writeByte(this.isClick ? (byte) 1 : (byte) 0);
        }

        public TimeDataBean() {
        }

        protected TimeDataBean(Parcel in) {
            this.years = in.readInt();
            this.times = in.readInt();
            this.timetext = in.readString();
            this.isClick = in.readByte() != 0;
        }

        public static final Parcelable.Creator<TimeDataBean> CREATOR = new Parcelable.Creator<TimeDataBean>() {
            @Override
            public TimeDataBean createFromParcel(Parcel source) {
                return new TimeDataBean(source);
            }

            @Override
            public TimeDataBean[] newArray(int size) {
                return new TimeDataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.timedata);
    }

    public TimeConfigResponse() {
    }

    protected TimeConfigResponse(Parcel in) {
        this.timedata = in.createTypedArrayList(TimeDataBean.CREATOR);
    }

    public static final Parcelable.Creator<TimeConfigResponse> CREATOR = new Parcelable.Creator<TimeConfigResponse>() {
        @Override
        public TimeConfigResponse createFromParcel(Parcel source) {
            return new TimeConfigResponse(source);
        }

        @Override
        public TimeConfigResponse[] newArray(int size) {
            return new TimeConfigResponse[size];
        }
    };
}
