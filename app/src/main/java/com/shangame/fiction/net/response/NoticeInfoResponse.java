package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2019/7/23
 */
public class NoticeInfoResponse implements Parcelable {
    /**
     * records : 1
     * total : 2
     * pagedata : [{"id":1,"title":"sample string 2","contenttext":"sample string 3","addtime":"sample string 4"},{"id":1,"title":"sample string 2","contenttext":"sample string 3","addtime":"sample string 4"}]
     */

    public int records;
    public int total;
    public List<PageDataBean> pagedata;

    public static class PageDataBean implements Parcelable {
        /**
         * id : 1
         * title : sample string 2
         * contenttext : sample string 3
         * addtime : sample string 4
         */

        public int id;
        public String title;
        public String contenttext;
        public String addtime;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.title);
            dest.writeString(this.contenttext);
            dest.writeString(this.addtime);
        }

        public PageDataBean() {
        }

        protected PageDataBean(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.contenttext = in.readString();
            this.addtime = in.readString();
        }

        public static final Creator<PageDataBean> CREATOR = new Creator<PageDataBean>() {
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
        dest.writeList(this.pagedata);
    }

    public NoticeInfoResponse() {
    }

    protected NoticeInfoResponse(Parcel in) {
        this.records = in.readInt();
        this.total = in.readInt();
        this.pagedata = new ArrayList<PageDataBean>();
        in.readList(this.pagedata, PageDataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<NoticeInfoResponse> CREATOR = new Parcelable.Creator<NoticeInfoResponse>() {
        @Override
        public NoticeInfoResponse createFromParcel(Parcel source) {
            return new NoticeInfoResponse(source);
        }

        @Override
        public NoticeInfoResponse[] newArray(int size) {
            return new NoticeInfoResponse[size];
        }
    };
}
