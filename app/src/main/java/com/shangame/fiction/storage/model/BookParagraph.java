package com.shangame.fiction.storage.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Create by Speedy on 2018/9/4
 */
@Entity
public class BookParagraph {

    @Id(autoincrement = true)
    public Long index;

    public long pid;

    public long bookId;

    public long chapterId;

    public String paragraph;


    public BookParagraph(){
        
    }


    @Generated(hash = 1384847486)
    public BookParagraph(Long index, long pid, long bookId, long chapterId,
            String paragraph) {
        this.index = index;
        this.pid = pid;
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.paragraph = paragraph;
    }


    public long getPid() {
        return this.pid;
    }


    public void setPid(long pid) {
        this.pid = pid;
    }


    public long getBookId() {
        return this.bookId;
    }


    public void setBookId(long bookId) {
        this.bookId = bookId;
    }


    public long getChapterId() {
        return this.chapterId;
    }


    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }


    public String getParagraph() {
        return this.paragraph;
    }


    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }


    public Long getIndex() {
        return this.index;
    }


    public void setIndex(Long index) {
        this.index = index;
    }



}
