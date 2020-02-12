package com.shangame.fiction.ui.author.home;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.NoticeInfoResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;

/**
 * Create by Speedy on 2019/7/23
 */
public interface HomeContacts {

    interface View extends BaseContract.BaseView {
        void getPicConfigSuccess(PictureConfigResponse pictureConfigResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getPicConfig(long userId);
    }
}
