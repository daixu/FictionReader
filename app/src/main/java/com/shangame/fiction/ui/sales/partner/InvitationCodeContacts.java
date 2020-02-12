package com.shangame.fiction.ui.sales.partner;

import com.shangame.fiction.core.base.BaseContract;

/**
 * Create by Speedy on 2019/7/23
 */
public interface InvitationCodeContacts {

    interface View extends BaseContract.BaseView {
        void bindAgentIdSuccess();

        void bindAgentIdFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void bindAgentId(long userId, int agentId);
    }
}
