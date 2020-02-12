package com.shangame.fiction.ui.signin;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.SignInInfoResponse;
import com.shangame.fiction.net.response.SignInResponse;

/**
 * Create by Speedy on 2018/7/23
 */
public interface SigninContract {

    interface View extends BaseContract.BaseView{
        void signInSuccess(SignInResponse signInResponse);
        void getSigninInfoSuccess(SignInInfoResponse signInInfoResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void signIn(long userid);
        void getSignInInfo(long userid);
    }
}
