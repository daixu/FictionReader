package com.shangame.fiction.ui.author.notice;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BookNoticeInfoResponse;
import com.shangame.fiction.net.response.NoticeInfoResponse;

/**
 * Create by Speedy on 2019/7/23
 */
public interface NoticeListContacts {

    interface View extends BaseContract.BaseView {
        void getNoticeInfoSuccess(NoticeInfoResponse response);

        void getNoticeInfoFailure(String msg);

        void getBookNoticeInfoSuccess(BookNoticeInfoResponse response);

        void getBookNoticeInfoFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getNoticeInfo(int page, int pageSize, int noticeType);

        void getBookNoticeInfo(int page, int pageSize, int bookId);
    }
}
