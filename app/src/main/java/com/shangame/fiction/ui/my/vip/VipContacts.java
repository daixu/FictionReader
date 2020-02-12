package com.shangame.fiction.ui.my.vip;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.VipInfoResponse;

/**
 * Create by Speedy on 2018/8/23
 */
public interface VipContacts {

    public interface View extends BaseContract.BaseView{
        void getVipInfoSuccess(VipInfoResponse vipInfoResponse);
    }

    public interface Prestener<V> extends BaseContract.BaserPresenter<V>{
        void getVipInfo(int userid);
    }
}
