package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Create by Speedy on 2018/8/22
 */
public class RecommendBookResponse {

    public List<RecdataBean> recdata;

    public static class RecdataBean implements Parcelable{
        public int bookid;
        public String bookname;
        public String author;
        public String bookcover;
        public String synopsis;
        public int chapterid;
        public int status;

        protected RecdataBean(Parcel in) {
            bookid = in.readInt();
            bookname = in.readString();
            author = in.readString();
            bookcover = in.readString();
            synopsis = in.readString();
            chapterid = in.readInt();
            status = in.readInt();
        }

        public static final Creator<RecdataBean> CREATOR = new Creator<RecdataBean>() {
            @Override
            public RecdataBean createFromParcel(Parcel in) {
                return new RecdataBean(in);
            }

            @Override
            public RecdataBean[] newArray(int size) {
                return new RecdataBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(bookid);
            dest.writeString(bookname);
            dest.writeString(author);
            dest.writeString(bookcover);
            dest.writeString(synopsis);
            dest.writeInt(chapterid);
            dest.writeInt(status);
        }
    }
}
