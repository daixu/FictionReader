package com.shangame.fiction.ui.setting.personal;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.UpLoadImageResponse;

/**
 * Create by Speedy on 2018/9/12
 */
public interface UploadContacts {

    interface View extends BaseContract.BaseView {
        void uploadImageSuccess(UpLoadImageResponse response);
    }

    interface Prestener<V> extends BaseContract.BaserPresenter<V> {
        void uploadImage(long userId, String imgPath);
    }
}
