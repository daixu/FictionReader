package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by Speedy on 2018/9/26
 */
public class VersionCheckResponse implements Parcelable{

    public String content;
    public int version;
    public String remark;
    public String dowurl;
    public int updatetype;
    public int verify;

    protected VersionCheckResponse(Parcel in) {
        content = in.readString();
        version = in.readInt();
        remark = in.readString();
        dowurl = in.readString();
        updatetype = in.readInt();
        verify = in.readInt();
    }

    public static final Creator<VersionCheckResponse> CREATOR = new Creator<VersionCheckResponse>() {
        @Override
        public VersionCheckResponse createFromParcel(Parcel in) {
            return new VersionCheckResponse(in);
        }

        @Override
        public VersionCheckResponse[] newArray(int size) {
            return new VersionCheckResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeInt(version);
        dest.writeString(remark);
        dest.writeString(dowurl);
        dest.writeInt(updatetype);
        dest.writeInt(verify);
    }
}
