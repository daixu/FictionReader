package com.shangame.fiction.ui.my.pay;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.CreatWapOrderResponse;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.net.response.GetRechargeConfigResponse;
import com.shangame.fiction.storage.model.UserInfo;

import java.util.Map;

/**
 * Create by Speedy on 2018/9/12
 */
public interface PayContracts {

    interface View extends BaseContract.BaseView {
        void getPayMethodsSuccess(GetPayMenthodsResponse response);

        void getRechargeConfigSuccess(GetRechargeConfigResponse response);

        void wapCreateOrderSuccess(CreatWapOrderResponse response, int payMethod);

        void getUserInfoSuccess(UserInfo userInfo);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getPayMethods();

        void getRechargeConfig(long userId);

        void createWapOrder(Map<String, Object> map, int payMethod);

        void getUserInfo(long userId);
    }
}
