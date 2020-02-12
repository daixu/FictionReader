package com.shangame.fiction.ui.setting.personal;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.storage.model.UserInfo;

import java.util.Map;

/**
 * Create by Speedy on 2018/8/24
 */
public interface PersonalContacts {

    interface View extends BaseContract.BaseView{
        void modifyProfileSuccess(UserInfo userInfo);
    }

    interface Prestener<V> extends BaseContract.BaserPresenter<V>{
        void modifyProfile(Map<String,Object> map);
    }
}
