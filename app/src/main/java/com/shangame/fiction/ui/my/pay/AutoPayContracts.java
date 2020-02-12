package com.shangame.fiction.ui.my.pay;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AutoPayResponse;

/**
 * Create by Speedy on 2019/1/5
 */
public interface AutoPayContracts {

    interface View extends BaseContract.BaseView{
        void setAutoPaySuccess();
        void getAutoPayListSuccess(AutoPayResponse autoPayResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getAutoPayList(long userid, int page,int pageSize);
        void setAutoPay(long userid,final AutoPayResponse.AutoPayItem autoPayItem,int autorenew);
    }
}
