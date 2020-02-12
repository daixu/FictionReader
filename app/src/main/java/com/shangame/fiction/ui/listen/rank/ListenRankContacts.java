package com.shangame.fiction.ui.listen.rank;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumRankingResponse;

/**
 * Create by Speedy on 2018/8/22
 *
 * @author hhh
 */
public interface ListenRankContacts {

    interface View extends BaseContract.BaseView {
        void getAlbumRankSuccess(AlbumRankingResponse rankResponse);
    }

    interface Prestener<V> extends BaseContract.BaserPresenter<V> {
        void getAlbumRank(int userId, int dayType);
    }
}
