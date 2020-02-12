package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by Speedy on 2019/7/22
 */
public class AuthorInfoResponse implements Parcelable {
    public int authorid;
    public int userid;
    public String headimgurl;
    public String nickname;
    public String penname;
    public String mobilephone;
    public String card;
    public String realname;
    public String address;
    public String email;
    public String qq;
    public int sex;
    public String province;
    public String city;
    public String addtime;
    public String synopsis;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.authorid);
        dest.writeInt(this.userid);
        dest.writeString(this.headimgurl);
        dest.writeString(this.nickname);
        dest.writeString(this.penname);
        dest.writeString(this.mobilephone);
        dest.writeString(this.card);
        dest.writeString(this.realname);
        dest.writeString(this.address);
        dest.writeString(this.email);
        dest.writeString(this.qq);
        dest.writeInt(this.sex);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.addtime);
        dest.writeString(this.synopsis);
    }

    public AuthorInfoResponse() {
    }

    protected AuthorInfoResponse(Parcel in) {
        this.authorid = in.readInt();
        this.userid = in.readInt();
        this.headimgurl = in.readString();
        this.nickname = in.readString();
        this.penname = in.readString();
        this.mobilephone = in.readString();
        this.card = in.readString();
        this.realname = in.readString();
        this.address = in.readString();
        this.email = in.readString();
        this.qq = in.readString();
        this.sex = in.readInt();
        this.province = in.readString();
        this.city = in.readString();
        this.addtime = in.readString();
        this.synopsis = in.readString();
    }

    public static final Parcelable.Creator<AuthorInfoResponse> CREATOR = new Parcelable.Creator<AuthorInfoResponse>() {
        @Override
        public AuthorInfoResponse createFromParcel(Parcel source) {
            return new AuthorInfoResponse(source);
        }

        @Override
        public AuthorInfoResponse[] newArray(int size) {
            return new AuthorInfoResponse[size];
        }
    };
}
