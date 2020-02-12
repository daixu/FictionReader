package com.shangame.fiction.ui.setting.personal.area;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.CityResponse;
import com.shangame.fiction.net.response.ProvinceResponse;

/**
 * Create by Speedy on 2018/8/27
 */
public interface AreaContracts {

    interface View extends BaseContract.BaseView{
        void getProvinceListSuccess(ProvinceResponse provinceResponse);
        void getCityListSuccess(CityResponse cityResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getProvinceList();
        void getCityList(int fId);
    }

}
