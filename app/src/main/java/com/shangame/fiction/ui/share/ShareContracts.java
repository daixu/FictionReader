package com.shangame.fiction.ui.share;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.ShareResponse;

/**
 * Create by Speedy on 2018/9/28
 */
public interface ShareContracts {

    interface View extends BaseContract.BaseView {
        void getBookShareInfoSuccess(ShareResponse shareResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getBookShareInfo(long userId, long bookId, long chapterId);
    }
}
