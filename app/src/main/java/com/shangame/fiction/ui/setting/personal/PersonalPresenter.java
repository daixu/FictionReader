package com.shangame.fiction.ui.setting.personal;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.storage.model.UserInfo;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/27
 */
public class PersonalPresenter extends RxPresenter<PersonalContacts.View> implements PersonalContacts.Prestener<PersonalContacts.View> {


    @Override
    public void modifyProfile(Map<String, Object> map) {
        if(mView != null){
            mView.showLoading();
        }
        Observable<HttpResult<UserInfo>> observable =  ApiManager.getInstance().modifyProfile(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<UserInfo>>() {
            @Override
            public void accept(HttpResult<UserInfo> result) throws Exception {
                if(mView != null){
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.modifyProfileSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }



}
