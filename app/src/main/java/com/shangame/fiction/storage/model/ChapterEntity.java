package com.shangame.fiction.storage.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2018/8/10
 */
public class ChapterEntity {
    public long chapterId;
    public String chapterName;
    public long chapterWordCount;
    public List<ParagraphEntity> paragraphEntityList = new ArrayList<>();
}
