package com.shangame.fiction.ui.author.works.type;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.ClassAllFigResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class WorksTypePresenter extends RxPresenter<WorksTypeContacts.View> implements WorksTypeContacts.Presenter<WorksTypeContacts.View> {

    @Override
    public void getClassAllFig(int maleChannel) {
        Observable<HttpResult<ClassAllFigResponse>> observable = ApiManager.getInstance().getClassAllFig(maleChannel);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ClassAllFigResponse>>() {
            @Override
            public void accept(HttpResult<ClassAllFigResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        if (null != result.data && null != result.data.superdata && result.data.superdata.size() > 0) {
                            mView.getClassAllFigSuccess(result.data);
                        } else {
                            mView.getClassAllFigFailure(result.msg);
                        }
                    } else {
                        mView.getClassAllFigFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
