package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by Speedy on 2018/8/20
 */
public class BookComment implements Parcelable {

    public int userid;
    public int comid;
    public String nickname;
    public String headimgurl;
    public int viplv;
    public String vipname;
    public String comment;
    public int pracount;
    public int replycount;
    public String creatortime;
    public int state;

    public BookComment(){

    };

    protected BookComment(Parcel in) {
        userid = in.readInt();
        comid = in.readInt();
        nickname = in.readString();
        headimgurl = in.readString();
        viplv = in.readInt();
        vipname = in.readString();
        comment = in.readString();
        pracount = in.readInt();
        replycount = in.readInt();
        creatortime = in.readString();
        state = in.readInt();
    }

    public static final Creator<BookComment> CREATOR = new Creator<BookComment>() {
        @Override
        public BookComment createFromParcel(Parcel in) {
            return new BookComment(in);
        }

        @Override
        public BookComment[] newArray(int size) {
            return new BookComment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(userid);
        parcel.writeInt(comid);
        parcel.writeString(nickname);
        parcel.writeString(headimgurl);
        parcel.writeInt(viplv);
        parcel.writeString(vipname);
        parcel.writeString(comment);
        parcel.writeInt(pracount);
        parcel.writeInt(replycount);
        parcel.writeString(creatortime);
        parcel.writeInt(state);
    }
}
