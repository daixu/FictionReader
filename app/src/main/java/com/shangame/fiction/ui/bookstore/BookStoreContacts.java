package com.shangame.fiction.ui.bookstore;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.ChoicenessResponse;
import com.shangame.fiction.net.response.FriendReadResponse;
import com.shangame.fiction.net.response.HomeStoreResponse;
import com.shangame.fiction.net.response.MaleChannelResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;

/**
 * Create by Speedy on 2018/8/13
 */
public interface BookStoreContacts {

    interface View extends BaseContract.BaseView{
        void getFriendReadSuccess(FriendReadResponse friendReadResponse);
        void getBookDataSuccess(MaleChannelResponse maleChannelResponse);
        void getPictureConfigSuccess(PictureConfigResponse pictureConfigResponse);

    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getBookData(int userid,int pagecount,int malechannel);
        void getFriendRead(long userid, int malechannel, int page, int pagesize, int status);
        void getPictureConfig(int userid,int malechannel);
    }
}
