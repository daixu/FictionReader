package com.shangame.fiction.ui.reader;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.CreatWapOrderResponse;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.net.response.ReadTimeResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.model.UserInfo;

import java.util.Map;

/**
 * Create by Speedy on 2018/9/3
 */
public interface GetUserInfoContracts {

    interface View extends BaseContract.BaseView {
        void getUserInfoSuccess(UserInfo userInfo);

        void getPayMethodsSuccess(GetPayMenthodsResponse response);

        void wapCreateOrderSuccess(CreatWapOrderResponse response, int payMethod);

        void setAddAdvertLogSuccess(long chapterId, int repOrType);

        void setReceiveLogSuccess(TaskAwardResponse response);

        void sendReadTimeSuccess(ReadTimeResponse response);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getUserInfo(long userId);

        void getPayMethods();

        void createWapOrder(Map<String, Object> map, int payMethod);

        void setAddAdvertLog(long userId, long bookId, long chapterId, int repOrType);

        void setReceiveLog(long userId, int taskLogId);

        void sendReadTime(long userId, int type);

        void sendOfflineReadTime(int type);
    }
}
