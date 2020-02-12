package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Create by Speedy on 2019/7/25
 */
public class ChapterResponse implements Parcelable {
    /**
     * records : 1
     * total : 2
     * pagedata : [{"cid":1,"bookid":2,"volume":3,"title":"sample string 4","text":"sample string 5","lastmodifytime":"sample string 6","releaseTime":"sample string 7","drafts":8,"wordnumber":9,"chapternumber":10,"authortext":"sample string 11"},{"cid":1,"bookid":2,"volume":3,"title":"sample string 4","text":"sample string 5","lastmodifytime":"sample string 6","releaseTime":"sample string 7","drafts":8,"wordnumber":9,"chapternumber":10,"authortext":"sample string 11"}]
     */

    public int records;
    public int total;
    public List<PageDataBean> pagedata;

    public static class PageDataBean implements Parcelable {
        /**
         * cid : 1
         * bookid : 2
         * volume : 3
         * title : sample string 4
         * text : sample string 5
         * lastmodifytime : sample string 6
         * releaseTime : sample string 7
         * drafts : 8
         * wordnumber : 9
         * chapternumber : 10
         * authortext : sample string 11
         */

        public int cid;
        public int bookid;
        public int volume;
        public String title;
        public String text;
        public String lastmodifytime;
        public String releaseTime;
        public int drafts;
        public int timetype;
        public int chargingmode;
        public int wordnumber;
        public int chapternumber;
        public String authortext;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.cid);
            dest.writeInt(this.bookid);
            dest.writeInt(this.volume);
            dest.writeString(this.title);
            dest.writeString(this.text);
            dest.writeString(this.lastmodifytime);
            dest.writeString(this.releaseTime);
            dest.writeInt(this.drafts);
            dest.writeInt(this.timetype);
            dest.writeInt(this.chargingmode);
            dest.writeInt(this.wordnumber);
            dest.writeInt(this.chapternumber);
            dest.writeString(this.authortext);
        }

        public PageDataBean() {
        }

        protected PageDataBean(Parcel in) {
            this.cid = in.readInt();
            this.bookid = in.readInt();
            this.volume = in.readInt();
            this.title = in.readString();
            this.text = in.readString();
            this.lastmodifytime = in.readString();
            this.releaseTime = in.readString();
            this.drafts = in.readInt();
            this.timetype = in.readInt();
            this.chargingmode = in.readInt();
            this.wordnumber = in.readInt();
            this.chapternumber = in.readInt();
            this.authortext = in.readString();
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

    public ChapterResponse() {
    }

    protected ChapterResponse(Parcel in) {
        this.records = in.readInt();
        this.total = in.readInt();
        this.pagedata = in.createTypedArrayList(PageDataBean.CREATOR);
    }

    public static final Parcelable.Creator<ChapterResponse> CREATOR = new Parcelable.Creator<ChapterResponse>() {
        @Override
        public ChapterResponse createFromParcel(Parcel source) {
            return new ChapterResponse(source);
        }

        @Override
        public ChapterResponse[] newArray(int size) {
            return new ChapterResponse[size];
        }
    };
}
