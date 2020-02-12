package com.shangame.fiction.net.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by Speedy on 2019/8/10
 */
@Entity
public class LocalChapterListBean implements  Cloneable {

    private String noteUrl; //对应BookInfoBean noteUrl;

    private int durChapterIndex;  //当前章节数
    @Id
    private String durChapterUrl;  //当前章节对应的文章地址

    private String durChapterName;  //当前章节名称

    private Boolean hasCache = false;

    @Transient
    private LocalBookContentBean localBookContentBean = new LocalBookContentBean();

    @Generated(hash = 675349910)
    public LocalChapterListBean(String noteUrl, int durChapterIndex,
            String durChapterUrl, String durChapterName, Boolean hasCache) {
        this.noteUrl = noteUrl;
        this.durChapterIndex = durChapterIndex;
        this.durChapterUrl = durChapterUrl;
        this.durChapterName = durChapterName;
        this.hasCache = hasCache;
    }

    @Generated(hash = 1697647718)
    public LocalChapterListBean() {
    }

    public LocalBookContentBean getLocalBookContentBean() {
        return localBookContentBean;
    }

    public void setLocalBookContentBean(LocalBookContentBean localBookContentBean) {
        this.localBookContentBean = localBookContentBean;
    }

    public String getNoteUrl() {
        return this.noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public int getDurChapterIndex() {
        return this.durChapterIndex;
    }

    public void setDurChapterIndex(int durChapterIndex) {
        this.durChapterIndex = durChapterIndex;
    }

    public String getDurChapterUrl() {
        return this.durChapterUrl;
    }

    public void setDurChapterUrl(String durChapterUrl) {
        this.durChapterUrl = durChapterUrl;
    }

    public String getDurChapterName() {
        return this.durChapterName;
    }

    public void setDurChapterName(String durChapterName) {
        this.durChapterName = durChapterName;
    }

    public Boolean getHasCache() {
        return this.hasCache;
    }

    public void setHasCache(Boolean hasCache) {
        this.hasCache = hasCache;
    }

}
