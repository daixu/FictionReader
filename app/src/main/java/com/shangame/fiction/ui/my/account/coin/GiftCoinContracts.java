package com.shangame.fiction.ui.my.account.coin;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.CoinSummaryResponse;

/**
 * Create by Speedy on 2019/1/3
 */
public interface GiftCoinContracts {

    interface View extends BaseContract.BaseView{
        void getCoinSummarySuccess(CoinSummaryResponse coinSummaryResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getCoinSummary(long userid);
    }
}
