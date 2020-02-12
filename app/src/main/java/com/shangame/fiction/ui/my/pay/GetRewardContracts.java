//package com.shangame.fiction.ui.my.pay;
//
//import com.shangame.fiction.core.base.BaseContract;
//import com.shangame.fiction.net.response.CreatWapOrderResponse;
//import com.shangame.fiction.net.response.GetPayMenthodsResponse;
//import com.shangame.fiction.net.response.GetRechargeConfigResponse;
//import com.shangame.fiction.net.response.GetRewardResponse;
//import com.shangame.fiction.net.response.TaskAwardResponse;
//
//import java.util.Map;
//
///**
// * Create by Speedy on 2018/9/12
// */
//public interface GetRewardContracts {
//
//    interface View extends BaseContract.BaseView{
//        void getRewardSuccess(TaskAwardResponse taskAwardResponse);
//
//    }
//
//    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
//        void getReward(long userid,int sharetype);
//    }
//}
