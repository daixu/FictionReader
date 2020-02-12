package com.shangame.fiction.storage.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class ChapterInfo {

    @Id(autoincrement = true)
    public Long id;

    public long cid;
    public long bookid;
    public String title;
    public int chaptecount;
    public int cwordnumber;
    public int bwordnumber;
    public int parlength;
    public long lastcid;
    public long nextcid;
    public int lastmodifytimes;
    public int bookshelves;//0为未加入，1为已加入
    public int chargingmode;
    public int buystatus;
    public int sort;
    public long readmoney;
    public long chapterprice;

    public String bookname;
    public String author;
    public String synopsis;
    public String bookcover;
    public String booksource;

    public int advertopen = 1;
    public int advertpage = 2;

    @Transient
    public int popopen;

    @Generated(hash = 33351977)
    public ChapterInfo(Long id, long cid, long bookid, String title,
            int chaptecount, int cwordnumber, int bwordnumber, int parlength,
            long lastcid, long nextcid, int lastmodifytimes, int bookshelves,
            int chargingmode, int buystatus, int sort, long readmoney,
            long chapterprice, String bookname, String author, String synopsis,
            String bookcover, String booksource, int advertopen, int advertpage) {
        this.id = id;
        this.cid = cid;
        this.bookid = bookid;
        this.title = title;
        this.chaptecount = chaptecount;
        this.cwordnumber = cwordnumber;
        this.bwordnumber = bwordnumber;
        this.parlength = parlength;
        this.lastcid = lastcid;
        this.nextcid = nextcid;
        this.lastmodifytimes = lastmodifytimes;
        this.bookshelves = bookshelves;
        this.chargingmode = chargingmode;
        this.buystatus = buystatus;
        this.sort = sort;
        this.readmoney = readmoney;
        this.chapterprice = chapterprice;
        this.bookname = bookname;
        this.author = author;
        this.synopsis = synopsis;
        this.bookcover = bookcover;
        this.booksource = booksource;
        this.advertopen = advertopen;
        this.advertpage = advertpage;
    }

    @Generated(hash = 1687309083)
    public ChapterInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCid() {
        return this.cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getBookid() {
        return this.bookid;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChaptecount() {
        return this.chaptecount;
    }

    public void setChaptecount(int chaptecount) {
        this.chaptecount = chaptecount;
    }

    public int getCwordnumber() {
        return this.cwordnumber;
    }

    public void setCwordnumber(int cwordnumber) {
        this.cwordnumber = cwordnumber;
    }

    public int getBwordnumber() {
        return this.bwordnumber;
    }

    public void setBwordnumber(int bwordnumber) {
        this.bwordnumber = bwordnumber;
    }

    public int getParlength() {
        return this.parlength;
    }

    public void setParlength(int parlength) {
        this.parlength = parlength;
    }

    public long getLastcid() {
        return this.lastcid;
    }

    public void setLastcid(long lastcid) {
        this.lastcid = lastcid;
    }

    public long getNextcid() {
        return this.nextcid;
    }

    public void setNextcid(long nextcid) {
        this.nextcid = nextcid;
    }

    public int getLastmodifytimes() {
        return this.lastmodifytimes;
    }

    public void setLastmodifytimes(int lastmodifytimes) {
        this.lastmodifytimes = lastmodifytimes;
    }

    public int getBookshelves() {
        return this.bookshelves;
    }

    public void setBookshelves(int bookshelves) {
        this.bookshelves = bookshelves;
    }

    public int getChargingmode() {
        return this.chargingmode;
    }

    public void setChargingmode(int chargingmode) {
        this.chargingmode = chargingmode;
    }

    public int getBuystatus() {
        return this.buystatus;
    }

    public void setBuystatus(int buystatus) {
        this.buystatus = buystatus;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getReadmoney() {
        return this.readmoney;
    }

    public void setReadmoney(long readmoney) {
        this.readmoney = readmoney;
    }

    public long getChapterprice() {
        return this.chapterprice;
    }

    public void setChapterprice(long chapterprice) {
        this.chapterprice = chapterprice;
    }

    public String getBookname() {
        return this.bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getBookcover() {
        return this.bookcover;
    }

    public void setBookcover(String bookcover) {
        this.bookcover = bookcover;
    }

    public String getBooksource() {
        return this.booksource;
    }

    public void setBooksource(String booksource) {
        this.booksource = booksource;
    }

    public int getAdvertopen() {
        return this.advertopen;
    }

    public void setAdvertopen(int advertopen) {
        this.advertopen = advertopen;
    }

    public int getAdvertpage() {
        return this.advertpage;
    }

    public void setAdvertpage(int advertpage) {
        this.advertpage = advertpage;
    }
    
}