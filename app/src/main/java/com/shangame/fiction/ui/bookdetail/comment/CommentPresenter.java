package com.shangame.fiction.ui.bookdetail.comment;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BookCommentByTypeResponse;
import com.shangame.fiction.net.response.BookDetailCommentResponse;
import com.shangame.fiction.net.response.CommentReplyResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.SendCommentResponse;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/20
 */
public class CommentPresenter extends RxPresenter<CommentContacts.View> implements CommentContacts.Presenter<CommentContacts.View> {

    @Override
    public void getBookComment(long userid, long bookid) {
        Observable<HttpResult<BookDetailCommentResponse>> observable =  ApiManager.getInstance().getBookComment(userid,bookid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<BookDetailCommentResponse>>() {
            @Override
            public void accept(HttpResult<BookDetailCommentResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result,mView)) {
                    mView.getBookCommentSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }


    @Override
    public void getBookCommentByType(long userid, long bookid,int type,int page, int pagesize) {
        Observable<HttpResult<BookCommentByTypeResponse>> observable =  ApiManager.getInstance().getBookCommentByType(userid,bookid,type,page,pagesize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<BookCommentByTypeResponse>>() {
            @Override
            public void accept(HttpResult<BookCommentByTypeResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result,mView)) {
                    mView.getBookCommentByTypeSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }


    @Override
    public void getCommentReplyList(long userid, long bookid, long cid, int page, int pagesize) {
        Observable<HttpResult<CommentReplyResponse>> observable =  ApiManager.getInstance().getCommentReplyList(userid,bookid,cid,page,pagesize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<CommentReplyResponse>>() {
            @Override
            public void accept(HttpResult<CommentReplyResponse> result) throws Exception {
                if (mView != null ) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.getCommentReplyListSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }



    @Override
    public void sendComment(long userid, long bookid, long parentid, long replyuserid, String comment) {

        if (mView != null){
            mView.showLoading();
        }

        Observable<HttpResult<SendCommentResponse>> observable =  ApiManager.getInstance().sendComment(userid,bookid,parentid,replyuserid,comment);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<SendCommentResponse>>() {
            @Override
            public void accept(HttpResult<SendCommentResponse> result) throws Exception {
                if (mView != null ) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.sendCommentSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void sendLike(Map<String,Object> map) {
        if (mView != null){
            mView.showLoading();
        }

        Observable<HttpResult<Object>> observable =  ApiManager.getInstance().sendLike(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null ) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.sendLikeSuccess();
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
