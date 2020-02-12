package com.shangame.fiction.ui.author.statistics;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.ReportFromResponse;
import com.shangame.fiction.net.response.TimeConfigResponse;

/**
 * Create by Speedy on 2019/7/23
 */
public interface StatisticsContacts {

    interface View extends BaseContract.BaseView {
        void getTimeConfigSuccess(TimeConfigResponse response);

        void getTimeConfigFailure(String msg);

        void getReportFromSuccess(ReportFromResponse response);

        void getReportFromEmpty();

        void getReportFromFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getTimeConfig(int timeType, int bookId);

        void getReportFrom(int timeType, int bookId, int source, int dateType, int years, int times);
    }
}
