package com.shangame.fiction.ui.author.works.setting;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BookDataBean;
import com.shangame.fiction.net.response.UpLoadImageResponse;

import java.util.Map;

/**
 * Create by Speedy on 2019/7/23
 */
public interface WorksSettingContacts {

    interface View extends BaseContract.BaseView {
        void getBookDataSuccess(BookDataBean bean);

        void getBookDataFailure(String msg);

        void updateBookSuccess();

        void updateBookFailure(String msg);

        void uploadCoverSuccess(UpLoadImageResponse response);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getBookData(int bookId);

        void updateBook(Map<String, Object> map);

        void uploadCover(int bookId, String imgPath);
    }
}
