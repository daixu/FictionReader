package com.shangame.fiction.ui.bookstore.featured;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.ChoicenessResponse;
import com.shangame.fiction.net.response.OthersLookResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;

/**
 * Create by Speedy on 2018/8/13
 */
public interface FeaturedContacts {

    interface View extends BaseContract.BaseView {
        void getOthersLookDataSuccess(OthersLookResponse response);

        void getFeaturedDataSuccess(ChoicenessResponse response);

        void getPictureConfigSuccess(PictureConfigResponse response);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void othersLookData(int userId, int maleChannel, int page, int pageSize, int status);

        void getFeaturedData(int userId, int pageCount);

        void getPictureConfig(int userId, int maleChannel);
    }
}
