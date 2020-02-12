package com.shangame.fiction.book.parser;

import android.graphics.Paint;

import com.shangame.fiction.book.page.Line;
import com.shangame.fiction.storage.model.BookParagraph;
import com.shangame.fiction.storage.model.ChapterInfo;

import java.util.List;

/**
 * Create by Speedy on 2018/8/9
 */
public interface ChapterParser {

    /**
     * 解析章节
     *
     * @return
     */
    void parseChapter(final ChapterInfo chapterInfo, final List<BookParagraph> bookParagraphList, ChapterParserListener chapterParserListener);

    /**
     * @param bookParagraph
     * @param paint
     * @param lineWidth
     * @return
     */
    List<Line> parserParagraph(BookParagraph bookParagraph, Paint paint, int lineWidth);


}
