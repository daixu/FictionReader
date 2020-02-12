package com.shangame.fiction.ui.bookdetail.comment;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BookCommentByTypeResponse;
import com.shangame.fiction.net.response.BookDetailCommentResponse;
import com.shangame.fiction.net.response.CommentReplyResponse;
import com.shangame.fiction.net.response.SendCommentResponse;

import java.util.Map;

/**
 * Create by Speedy on 2018/8/20
 */
public class CommentContacts {

    public interface View extends BaseContract.BaseView{
        void getBookCommentSuccess(BookDetailCommentResponse bookDetailCommentResponse);
        void getBookCommentByTypeSuccess(BookCommentByTypeResponse bookCommentByTypeResponse);
        void getCommentReplyListSuccess(CommentReplyResponse commentReplyResponse);
        void sendCommentSuccess(SendCommentResponse sendCommentResponse);
        void sendLikeSuccess();
    }

    public interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getBookComment(long userid, long bookid);
        void getBookCommentByType(long userid, long bookid,int type,int page, int pagesize);
        void getCommentReplyList(long userid ,long bookid,long cid,int page ,int pagesize);
        void sendComment(long userid,long bookid,long parentid,long replyuserid,String comment);
        void sendLike(Map<String,Object> map);
    }
}
