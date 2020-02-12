package com.shangame.fiction.storage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by Speedy on 2018/8/13
 */
public class BookDetail implements Parcelable {

    public long bookid;
    public String bookname;
    public String wordnumbers;
    public String lastmodifytimes;
    public String classname;
    public String author;
    public String synopsis;
    public String bookcover;
    public int chapternumber;
    public String serstatus;
    public String subnumbers;
    public String giftnumbers;
    public long clicknumber;

    public BookDetail(){

    }

    protected BookDetail(Parcel in) {
        bookid = in.readLong();
        bookname = in.readString();
        wordnumbers = in.readString();
        lastmodifytimes = in.readString();
        classname = in.readString();
        author = in.readString();
        synopsis = in.readString();
        bookcover = in.readString();
        chapternumber = in.readInt();
        serstatus = in.readString();
        subnumbers = in.readString();
        giftnumbers = in.readString();
        clicknumber = in.readLong();
    }

    public static final Creator<BookDetail> CREATOR = new Creator<BookDetail>() {
        @Override
        public BookDetail createFromParcel(Parcel in) {
            return new BookDetail(in);
        }

        @Override
        public BookDetail[] newArray(int size) {
            return new BookDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(bookid);
        parcel.writeString(bookname);
        parcel.writeString(wordnumbers);
        parcel.writeString(lastmodifytimes);
        parcel.writeString(classname);
        parcel.writeString(author);
        parcel.writeString(synopsis);
        parcel.writeString(bookcover);
        parcel.writeInt(chapternumber);
        parcel.writeString(serstatus);
        parcel.writeString(subnumbers);
        parcel.writeString(giftnumbers);
        parcel.writeLong(clicknumber);
    }
}
