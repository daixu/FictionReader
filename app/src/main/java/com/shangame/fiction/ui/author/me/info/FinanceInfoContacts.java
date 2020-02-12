package com.shangame.fiction.ui.author.me.info;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.FinanceDataResponse;

import java.util.Map;

/**
 * Create by Speedy on 2019/7/23
 */
public interface FinanceInfoContacts {

    interface View extends BaseContract.BaseView {
        void getFinanceDataSuccess(FinanceDataResponse response);

        void getFinanceDataFailure(String msg);

        void setFinanceDataSuccess();

        void setFinanceDataFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getFinanceData(long userId);

        void setFinanceData(Map<String, Object> map);
    }
}
