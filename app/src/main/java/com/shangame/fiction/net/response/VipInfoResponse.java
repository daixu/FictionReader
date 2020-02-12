package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Create by Speedy on 2018/8/23
 */
public class VipInfoResponse implements Parcelable {

    public static final Creator<VipInfoResponse> CREATOR = new Creator<VipInfoResponse>() {
        @Override
        public VipInfoResponse createFromParcel(Parcel in) {
            return new VipInfoResponse(in);
        }

        @Override
        public VipInfoResponse[] newArray(int size) {
            return new VipInfoResponse[size];
        }
    };
    public int viplv;
    public int vipvalue;
    public String vipname;
    public List<VipPrivilegeBean> cfgentity;

    public VipInfoResponse() {

    }

    protected VipInfoResponse(Parcel in) {
        viplv = in.readInt();
        vipvalue = in.readInt();
        vipname = in.readString();
        cfgentity = in.createTypedArrayList(VipPrivilegeBean.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(viplv);
        dest.writeInt(vipvalue);
        dest.writeString(vipname);
        dest.writeTypedList(cfgentity);
    }

    public static class VipPrivilegeBean implements Parcelable {
        public static final Creator<VipPrivilegeBean> CREATOR = new Creator<VipPrivilegeBean>() {
            @Override
            public VipPrivilegeBean createFromParcel(Parcel in) {
                return new VipPrivilegeBean(in);
            }

            @Override
            public VipPrivilegeBean[] newArray(int size) {
                return new VipPrivilegeBean[size];
            }
        };
        public int privilegeid;
        public String privilegename;
        public String reward;
        public String describe;
        public int state;

        public VipPrivilegeBean() {

        }

        protected VipPrivilegeBean(Parcel in) {
            privilegeid = in.readInt();
            privilegename = in.readString();
            reward = in.readString();
            describe = in.readString();
            state = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(privilegeid);
            parcel.writeString(privilegename);
            parcel.writeString(reward);
            parcel.writeString(describe);
            parcel.writeInt(state);
        }
    }
}
