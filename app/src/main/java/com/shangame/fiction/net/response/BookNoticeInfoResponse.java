package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Create by Speedy on 2019/8/1
 */
public class BookNoticeInfoResponse implements Parcelable {
    /**
     * records : 1
     * total : 2
     * pagedata : [{"bookid":1,"title":"sample string 2","contenttext":"sample string 3","noticetype":4,"addresser":"sample string 5","addtime":"sample string 6"},{"bookid":1,"title":"sample string 2","contenttext":"sample string 3","noticetype":4,"addresser":"sample string 5","addtime":"sample string 6"}]
     */

    public int records;
    public int total;
    public List<PageDataBean> pagedata;

    public static class PageDataBean implements Parcelable {
        /**
         * bookid : 1
         * title : sample string 2
         * contenttext : sample string 3
         * noticetype : 4
         * addresser : sample string 5
         * addtime : sample string 6
         */

        public int bookid;
        public String title;
        public String contenttext;
        public int noticetype;
        public String addresser;
        public String addtime;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.bookid);
            dest.writeString(this.title);
            dest.writeString(this.contenttext);
            dest.writeInt(this.noticetype);
            dest.writeString(this.addresser);
            dest.writeString(this.addtime);
        }

        public PageDataBean() {
        }

        protected PageDataBean(Parcel in) {
            this.bookid = in.readInt();
            this.title = in.readString();
            this.contenttext = in.readString();
            this.noticetype = in.readInt();
            this.addresser = in.readString();
            this.addtime = in.readString();
        }

        public static final Parcelable.Creator<PageDataBean> CREATOR = new Parcelable.Creator<PageDataBean>() {
            @Override
            public PageDataBean createFromParcel(Parcel source) {
                return new PageDataBean(source);
            }

            @Override
            public PageDataBean[] newArray(int size) {
                return new PageDataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.records);
        dest.writeInt(this.total);
        dest.writeTypedList(this.pagedata);
    }

    public BookNoticeInfoResponse() {
    }

    protected BookNoticeInfoResponse(Parcel in) {
        this.records = in.readInt();
        this.total = in.readInt();
        this.pagedata = in.createTypedArrayList(PageDataBean.CREATOR);
    }

    public static final Parcelable.Creator<BookNoticeInfoResponse> CREATOR = new Parcelable.Creator<BookNoticeInfoResponse>() {
        @Override
        public BookNoticeInfoResponse createFromParcel(Parcel source) {
            return new BookNoticeInfoResponse(source);
        }

        @Override
        public BookNoticeInfoResponse[] newArray(int size) {
            return new BookNoticeInfoResponse[size];
        }
    };
}
