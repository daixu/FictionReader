package com.shangame.fiction.storage.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by Speedy on 2018/8/31
 */
@Entity
public class BookBrowseHistory {

    public String boId;
    @Id
    public long bookid;
    public String bookname;
    public String bookcover;
    public long readTime;
    public int bookshelves;//判断是否加入书架 0为未加入，1为已加入
    public long chapteId;
    public int pageIndex;
    public int chapternumber;
    public boolean isLocal;
    public int booktype;

    @Generated(hash = 2137233176)
    public BookBrowseHistory(String boId, long bookid, String bookname,
            String bookcover, long readTime, int bookshelves, long chapteId,
            int pageIndex, int chapternumber, boolean isLocal, int booktype) {
        this.boId = boId;
        this.bookid = bookid;
        this.bookname = bookname;
        this.bookcover = bookcover;
        this.readTime = readTime;
        this.bookshelves = bookshelves;
        this.chapteId = chapteId;
        this.pageIndex = pageIndex;
        this.chapternumber = chapternumber;
        this.isLocal = isLocal;
        this.booktype = booktype;
    }
    @Generated(hash = 323418709)
    public BookBrowseHistory() {
    }
    public String getBoId() {
        return this.boId;
    }
    public void setBoId(String boId) {
        this.boId = boId;
    }
    public long getBookid() {
        return this.bookid;
    }
    public void setBookid(long bookid) {
        this.bookid = bookid;
    }
    public String getBookname() {
        return this.bookname;
    }
    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
    public String getBookcover() {
        return this.bookcover;
    }
    public void setBookcover(String bookcover) {
        this.bookcover = bookcover;
    }
    public long getReadTime() {
        return this.readTime;
    }
    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }
    public int getBookshelves() {
        return this.bookshelves;
    }
    public void setBookshelves(int bookshelves) {
        this.bookshelves = bookshelves;
    }
    public long getChapteId() {
        return this.chapteId;
    }
    public void setChapteId(long chapteId) {
        this.chapteId = chapteId;
    }
    public int getPageIndex() {
        return this.pageIndex;
    }
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
    public int getChapternumber() {
        return this.chapternumber;
    }
    public void setChapternumber(int chapternumber) {
        this.chapternumber = chapternumber;
    }
    public boolean getIsLocal() {
        return this.isLocal;
    }
    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }
    public int getBooktype() {
        return this.booktype;
    }
    public void setBooktype(int booktype) {
        this.booktype = booktype;
    }
}
