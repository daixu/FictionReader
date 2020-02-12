package com.shangame.fiction.ui.my.account.coin;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.CoinListResponse;

/**
 * Create by Speedy on 2019/1/3
 */
public class CoinListContracts {

    interface View extends BaseContract.BaseView{
        void getCoinListSuccess(CoinListResponse coinListResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getCoinList(long userid,int state,int page,int pageSize);
    }
}
