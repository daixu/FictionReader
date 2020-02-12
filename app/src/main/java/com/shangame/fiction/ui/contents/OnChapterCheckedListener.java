package com.shangame.fiction.ui.contents;

import com.shangame.fiction.storage.model.Chapter;

public interface OnChapterCheckedListener {
    void checkedChapter(Chapter chapter, int type);
}