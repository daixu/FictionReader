package com.shangame.fiction.storage.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public  class BookListEnitiy implements Parcelable{

        public int mid;
        public String title;
        public String contents;
        public String classname;
        public String bookcount;

        public List<BookcoverBean> bookcover;

        protected BookListEnitiy(Parcel in) {
                mid = in.readInt();
                title = in.readString();
                contents = in.readString();
                classname = in.readString();
                bookcount = in.readString();
        }

        public static final Creator<BookListEnitiy> CREATOR = new Creator<BookListEnitiy>() {
                @Override
                public BookListEnitiy createFromParcel(Parcel in) {
                        return new BookListEnitiy(in);
                }

                @Override
                public BookListEnitiy[] newArray(int size) {
                        return new BookListEnitiy[size];
                }
        };

        @Override
        public int describeContents() {
                return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(mid);
                dest.writeString(title);
                dest.writeString(contents);
                dest.writeString(classname);
                dest.writeString(bookcount);
        }

        public static class BookcoverBean {
                public String bookcover;
        }
}