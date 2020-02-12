package com.shangame.fiction.ui.author.data;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.storage.model.UserInfo;

import java.util.Map;

/**
 * Create by Speedy on 2019/7/23
 */
public interface DetailDataContacts {

    interface View extends BaseContract.BaseView {
        void setAuthorInfoSuccess();

        void setAuthorInfoSuccess(UserInfo userInfo);

        void setAuthorInfoFailure(String msg);
    }

    interface Prestener<V> extends BaseContract.BaserPresenter<V> {
        void setAuthorInfo(Map<String, Object> map);
    }
}
