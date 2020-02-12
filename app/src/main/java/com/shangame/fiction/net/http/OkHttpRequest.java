//package com.shangame.fiction.net.http;
//
//import android.support.annotation.NonNull;
//
//import com.shangame.fiction.net.manager.HttpManager;
//
//import java.util.Map;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableEmitter;
//import io.reactivex.ObservableOnSubscribe;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.Call;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
///**
// * Create by Speedy on 2018/9/11
// */
//public class OkHttpRequest implements HttpRequest {
//
//    private Disposable disposable;
//    private Call call;
//
//
//    @Override
//    public void doPost(String url, Map<String, String> params, OnRequestCallback requestCallback) {
//
//    }
//
//    @Override
//    public void doGet(String url, Map<String, String> params, OnRequestCallback requestCallback) {
//        OkHttpClient okHttpClient = HttpManager.getOkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//        call = okHttpClient.newCall(request);
//        executeCall(call, requestCallback);
//    }
//
//
//
//
//    private void executeCall(final Call call, final OnRequestCallback requestCallback) {
//        disposable = Observable.create(new ObservableOnSubscribe<String>() {
//
//            @Override
//            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
//                if (!call.isCanceled()) {
//                    try {
//                        Response response = call.execute();
//                        int statusCode = response.code();
//                        if (statusCode == 200) {
//                            String result = response.body().string();
//                            if(!observableEmitter.isDisposed()){
//                                observableEmitter.onNext(result);
//                                observableEmitter.onComplete();
//                            }
//                        } else {
//                            if(!observableEmitter.isDisposed()){
//                                observableEmitter.onError(new Exception("请求失败"));
//                            }
//                        }
//                    } catch (Exception ex) {
//                        if(!observableEmitter.isDisposed()){
//                            observableEmitter.onError(new Exception("请求失败:"+ex.getMessage()));
//                        }
//                    }
//                }else{
//                    if(!observableEmitter.isDisposed()){
//                        observableEmitter.onComplete();
//                    }
//                }
//
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(@NonNull String response) throws Exception {
//                        if (requestCallback != null) {
//                            requestCallback.onResponse(response);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        if (requestCallback != null) {
//                            requestCallback.onErrorResponse("请求失败:"+throwable.getMessage());
//                        }
//                    }
//                });
//    }
//
//
//
//
//    @Override
//    public void onCancel() {
//        if (call != null && call.isExecuted()) {
//            call.cancel();
//        }
//        if(disposable != null && !disposable.isDisposed()){
//            disposable.dispose();
//        }
//    }
//}
