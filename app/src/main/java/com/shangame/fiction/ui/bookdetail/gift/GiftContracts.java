package com.shangame.fiction.ui.bookdetail.gift;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.GetGiftListConfigResponse;
import com.shangame.fiction.net.response.GiveGiftResponse;
import com.shangame.fiction.net.response.ReceivedGiftResponse;

/**
 * Create by Speedy on 2018/8/29
 */
public interface GiftContracts {


    interface View extends BaseContract.BaseView{
        void getGiftListConfigSuccess(GetGiftListConfigResponse getGiftListConfigResponse);
        void giveGiftSuccess(GiveGiftResponse giveGiftResponse);
        void getReceivedGiftListSuccess(ReceivedGiftResponse receivedGiftResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getGiftListConfig(long userid);
        void giveGift(long userid,int propid,int pronumber,long bookid);
        void getReceivedGiftList(long bookid,int page,int pagesize);
    }
}
