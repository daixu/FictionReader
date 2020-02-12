package com.shangame.fiction.ui.reader.local;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.ui.reader.local.bean.BookChapterBean;
import com.shangame.fiction.ui.reader.local.page.TxtChapter;

import java.util.List;

public interface ReadContract extends BaseContract {
    interface View extends BaseContract.BaseView {
        void showCategory(List<BookChapterBean> bookChapterList);

        void finishChapter();

        void errorChapter();

        void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskId);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void loadCategory(String bookId);

        void loadChapter(String bookId, List<TxtChapter> bookChapterList);

        void getTaskAward(long userId,int taskId,boolean showLoading);
    }
}