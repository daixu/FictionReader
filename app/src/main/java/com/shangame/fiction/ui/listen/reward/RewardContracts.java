package com.shangame.fiction.ui.listen.reward;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.GetGiftListConfigResponse;
import com.shangame.fiction.net.response.GiveGiftResponse;
import com.shangame.fiction.net.response.ReceivedGiftResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;

/**
 * Create by Speedy on 2018/8/29
 *
 * @author hhh
 */
public interface RewardContracts {

    interface View extends BaseContract.BaseView {
        void getGiftConfigSuccess(GetGiftListConfigResponse response);

        void rewardGiftSuccess(GiveGiftResponse response);

        void getReceivedGiftSuccess(ReceivedGiftResponse response);

        void getTaskAwardSuccess(TaskAwardResponse response, int taskId);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getGiftConfig(long userId);

        void rewardGift(long userId, int proPid, int proNumber, long bookId);

        void getReceivedGift(long bookId, int page, int pageSize);

        void getTaskAward(long userId, int taskId, boolean showLoading);
    }
}
