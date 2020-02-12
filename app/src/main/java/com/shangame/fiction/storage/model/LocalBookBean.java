package com.shangame.fiction.storage.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by Speedy on 2019/8/8
 */
@Entity
public class LocalBookBean implements Parcelable {

    @Id
    public String strId;
    public long bookId;
    public int chapterNumber;
    public long chapterId;
    public String bookCover;
    public String bookName;
    public String author;
    public int updState;
    public int recState;
    public boolean isPicked;
    public int updChapter;
    public int updating;
    public long lastModifyTime;
    public int booktype;

    //是否是本地文件
    public boolean isLocal = false;
    public String path;
    @Generated(hash = 2109084725)
    public LocalBookBean(String strId, long bookId, int chapterNumber,
            long chapterId, String bookCover, String bookName, String author,
            int updState, int recState, boolean isPicked, int updChapter,
            int updating, long lastModifyTime, int booktype, boolean isLocal,
            String path) {
        this.strId = strId;
        this.bookId = bookId;
        this.chapterNumber = chapterNumber;
        this.chapterId = chapterId;
        this.bookCover = bookCover;
        this.bookName = bookName;
        this.author = author;
        this.updState = updState;
        this.recState = recState;
        this.isPicked = isPicked;
        this.updChapter = updChapter;
        this.updating = updating;
        this.lastModifyTime = lastModifyTime;
        this.booktype = booktype;
        this.isLocal = isLocal;
        this.path = path;
    }
    @Generated(hash = 464515480)
    public LocalBookBean() {
    }
    public String getStrId() {
        return this.strId;
    }
    public void setStrId(String strId) {
        this.strId = strId;
    }
    public long getBookId() {
        return this.bookId;
    }
    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
    public int getChapterNumber() {
        return this.chapterNumber;
    }
    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }
    public long getChapterId() {
        return this.chapterId;
    }
    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }
    public String getBookCover() {
        return this.bookCover;
    }
    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }
    public String getBookName() {
        return this.bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getUpdState() {
        return this.updState;
    }
    public void setUpdState(int updState) {
        this.updState = updState;
    }
    public int getRecState() {
        return this.recState;
    }
    public void setRecState(int recState) {
        this.recState = recState;
    }
    public boolean getIsPicked() {
        return this.isPicked;
    }
    public void setIsPicked(boolean isPicked) {
        this.isPicked = isPicked;
    }
    public int getUpdChapter() {
        return this.updChapter;
    }
    public void setUpdChapter(int updChapter) {
        this.updChapter = updChapter;
    }
    public int getUpdating() {
        return this.updating;
    }
    public void setUpdating(int updating) {
        this.updating = updating;
    }
    public long getLastModifyTime() {
        return this.lastModifyTime;
    }
    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
    public int getBooktype() {
        return this.booktype;
    }
    public void setBooktype(int booktype) {
        this.booktype = booktype;
    }
    public boolean getIsLocal() {
        return this.isLocal;
    }
    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.strId);
        dest.writeLong(this.bookId);
        dest.writeInt(this.chapterNumber);
        dest.writeLong(this.chapterId);
        dest.writeString(this.bookCover);
        dest.writeString(this.bookName);
        dest.writeString(this.author);
        dest.writeInt(this.updState);
        dest.writeInt(this.recState);
        dest.writeByte(this.isPicked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.updChapter);
        dest.writeInt(this.updating);
        dest.writeLong(this.lastModifyTime);
        dest.writeInt(this.booktype);
        dest.writeByte(this.isLocal ? (byte) 1 : (byte) 0);
        dest.writeString(this.path);
    }

    protected LocalBookBean(Parcel in) {
        this.strId = in.readString();
        this.bookId = in.readLong();
        this.chapterNumber = in.readInt();
        this.chapterId = in.readLong();
        this.bookCover = in.readString();
        this.bookName = in.readString();
        this.author = in.readString();
        this.updState = in.readInt();
        this.recState = in.readInt();
        this.isPicked = in.readByte() != 0;
        this.updChapter = in.readInt();
        this.updating = in.readInt();
        this.lastModifyTime = in.readLong();
        this.booktype = in.readInt();
        this.isLocal = in.readByte() != 0;
        this.path = in.readString();
    }

    public static final Parcelable.Creator<LocalBookBean> CREATOR = new Parcelable.Creator<LocalBookBean>() {
        @Override
        public LocalBookBean createFromParcel(Parcel source) {
            return new LocalBookBean(source);
        }

        @Override
        public LocalBookBean[] newArray(int size) {
            return new LocalBookBean[size];
        }
    };
}
