package com.shangame.fiction.ui.author.works.type;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.ClassAllFigResponse;

/**
 * Create by Speedy on 2019/7/23
 */
public interface WorksTypeContacts {

    interface View extends BaseContract.BaseView {
        void getClassAllFigSuccess(ClassAllFigResponse response);

        void getClassAllFigFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getClassAllFig(int maleChannel);
    }
}
