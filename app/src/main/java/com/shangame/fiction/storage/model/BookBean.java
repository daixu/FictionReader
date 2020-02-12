package com.shangame.fiction.storage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by Speedy on 2018/7/20
 */
public class BookBean implements Parcelable {
    public static final Parcelable.Creator<BookBean> CREATOR = new Parcelable.Creator<BookBean>() {
        @Override
        public BookBean createFromParcel(Parcel source) {
            return new BookBean(source);
        }

        @Override
        public BookBean[] newArray(int size) {
            return new BookBean[size];
        }
    };
    public long bookid;
    public int chapternumber;
    public long chapterid;
    public String bookcover;
    public String bookname;
    public String author;
    public int updstate;
    public int recstate;
    public boolean isPicked;
    public int updchapter;
    public int updating;
    public long lastmodifyTime;
    public int booktype;

    public BookBean() {
    }

    protected BookBean(Parcel in) {
        this.bookid = in.readLong();
        this.chapternumber = in.readInt();
        this.chapterid = in.readLong();
        this.bookcover = in.readString();
        this.bookname = in.readString();
        this.author = in.readString();
        this.updstate = in.readInt();
        this.recstate = in.readInt();
        this.isPicked = in.readByte() != 0;
        this.updchapter = in.readInt();
        this.updating = in.readInt();
        this.lastmodifyTime = in.readLong();
        this.booktype = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.bookid);
        dest.writeInt(this.chapternumber);
        dest.writeLong(this.chapterid);
        dest.writeString(this.bookcover);
        dest.writeString(this.bookname);
        dest.writeString(this.author);
        dest.writeInt(this.updstate);
        dest.writeInt(this.recstate);
        dest.writeByte(this.isPicked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.updchapter);
        dest.writeInt(this.updating);
        dest.writeLong(this.lastmodifyTime);
        dest.writeInt(this.booktype);
    }
}
