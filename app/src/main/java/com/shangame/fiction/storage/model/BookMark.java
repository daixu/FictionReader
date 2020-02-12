package com.shangame.fiction.storage.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by Speedy on 2018/8/10
 */
@Entity
public class BookMark implements Parcelable {

    @Id
    public String markId;

    public long index;
    public long userid;
    public long bookid;
    public long chapterid;
    public long pid;
    public String title;
    public String content;
    public String createtime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.markId);
        dest.writeLong(this.index);
        dest.writeLong(this.userid);
        dest.writeLong(this.bookid);
        dest.writeLong(this.chapterid);
        dest.writeLong(this.pid);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.createtime);
    }

    public String getMarkId() {
        return this.markId;
    }

    public void setMarkId(String markId) {
        this.markId = markId;
    }

    public long getIndex() {
        return this.index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public long getUserid() {
        return this.userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getBookid() {
        return this.bookid;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    public long getChapterid() {
        return this.chapterid;
    }

    public void setChapterid(long chapterid) {
        this.chapterid = chapterid;
    }

    public long getPid() {
        return this.pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public BookMark() {
    }

    protected BookMark(Parcel in) {
        this.markId = in.readString();
        this.index = in.readLong();
        this.userid = in.readLong();
        this.bookid = in.readLong();
        this.chapterid = in.readLong();
        this.pid = in.readLong();
        this.title = in.readString();
        this.content = in.readString();
        this.createtime = in.readString();
    }

    @Generated(hash = 870597102)
    public BookMark(String markId, long index, long userid, long bookid, long chapterid, long pid,
            String title, String content, String createtime) {
        this.markId = markId;
        this.index = index;
        this.userid = userid;
        this.bookid = bookid;
        this.chapterid = chapterid;
        this.pid = pid;
        this.title = title;
        this.content = content;
        this.createtime = createtime;
    }

    public static final Parcelable.Creator<BookMark> CREATOR = new Parcelable.Creator<BookMark>() {
        @Override
        public BookMark createFromParcel(Parcel source) {
            return new BookMark(source);
        }

        @Override
        public BookMark[] newArray(int size) {
            return new BookMark[size];
        }
    };
}
