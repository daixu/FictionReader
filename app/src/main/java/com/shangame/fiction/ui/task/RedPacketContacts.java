package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.CashConfigResponse;
import com.shangame.fiction.net.response.RedListResponse;
import com.shangame.fiction.net.response.WeChatCashResponse;

/**
 * Create by Speedy on 2019/3/30
 */
public interface RedPacketContacts {

    interface View extends BaseContract.BaseView {
        void getRedListSuccess(RedListResponse redListResponse);

        void getCashconfigSuccess(CashConfigResponse cashConfigResponse);

        void weChatCashSuccess(WeChatCashResponse wechatCashResponse, String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getRedList(long userid, String datatime, int page, int pageSize);

        void getCashConfig(long userid);

        void weChatCash(long userid, int cashid, String appid);
    }
}
