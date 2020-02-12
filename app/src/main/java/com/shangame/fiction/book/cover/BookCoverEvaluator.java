package com.shangame.fiction.book.cover;

import android.animation.TypeEvaluator;

/**
 * Create by Speedy on 2018/7/24
 */
public class BookCoverEvaluator implements TypeEvaluator<BookCoverValue> {


    @Override
    public BookCoverValue evaluate(float fraction, BookCoverValue startValue, BookCoverValue endValue) {
        int startLeft = startValue.getLeft();
        int startTop = startValue.getTop();
        int startRight = startValue.getRight();
        int startBottom = startValue.getBottom();
        int endLeft = endValue.getLeft();
        int endTop = endValue.getTop();
        int endRight = endValue.getRight();
        int endBottom = endValue.getBottom();

        BookCoverValue bookCoverValue = new BookCoverValue();
        bookCoverValue.setLeft((int) (startLeft - fraction * (startLeft - endLeft)));
        bookCoverValue.setTop((int) (startTop - fraction * (startTop - endTop)));
        bookCoverValue.setRight((int) (startRight - fraction * (startRight - endRight)));
        bookCoverValue.setBottom((int) (startBottom - fraction * (startBottom - endBottom)));

        return bookCoverValue;
    }
}
