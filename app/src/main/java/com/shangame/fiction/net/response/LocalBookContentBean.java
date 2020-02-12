package com.shangame.fiction.net.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by Speedy on 2019/8/10
 */
@Entity
public class LocalBookContentBean  {

    @Id
    private String durChapterUrl; //对应BookInfoBean noteUrl;

    private int durChapterIndex;   //当前章节

    private String durChapterContent; //当前章节内容

    @Transient
    private List<String> lineContent = new ArrayList<>();

    @Transient
    private float lineSize;

    @Generated(hash = 1469025273)
    public LocalBookContentBean(String durChapterUrl, int durChapterIndex,
            String durChapterContent) {
        this.durChapterUrl = durChapterUrl;
        this.durChapterIndex = durChapterIndex;
        this.durChapterContent = durChapterContent;
    }

    @Generated(hash = 1896865610)
    public LocalBookContentBean() {
    }

    public String getDurChapterUrl() {
        return this.durChapterUrl;
    }

    public void setDurChapterUrl(String durChapterUrl) {
        this.durChapterUrl = durChapterUrl;
    }

    public int getDurChapterIndex() {
        return this.durChapterIndex;
    }

    public void setDurChapterIndex(int durChapterIndex) {
        this.durChapterIndex = durChapterIndex;
    }

    public String getDurChapterContent() {
        return this.durChapterContent;
    }

    public void setDurChapterContent(String durChapterContent) {
        this.durChapterContent = durChapterContent;
    }
}
