package com.shangame.fiction.ui.rank;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.RankResponse;

/**
 * Create by Speedy on 2018/8/22
 */
public interface RankContacts {

    public interface View extends BaseContract.BaseView{
        void getRankListSuccess(RankResponse rankResponse);
    }

    public interface Prestener<V> extends BaseContract.BaserPresenter<V>{
        void getRankList(int userid,int malechannel,int daytype);
    }
}
