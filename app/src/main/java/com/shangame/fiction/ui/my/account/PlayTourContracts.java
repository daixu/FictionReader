package com.shangame.fiction.ui.my.account;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.PlayTourResponse;

/**
 * Create by Speedy on 2018/8/27
 */
public interface PlayTourContracts {

    interface View extends BaseContract.BaseView{
        void getPlayTourListSuccess(PlayTourResponse playTourResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getPlayTourList(int userid,int page,int pageSize);
    }
}
