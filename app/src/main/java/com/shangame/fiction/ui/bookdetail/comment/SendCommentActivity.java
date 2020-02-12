package com.shangame.fiction.ui.bookdetail.comment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.BookCommentByTypeResponse;
import com.shangame.fiction.net.response.BookDetailCommentResponse;
import com.shangame.fiction.net.response.CommentReplyResponse;
import com.shangame.fiction.net.response.SendCommentResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;

/**
 * 发送评论
 * Create by Speedy on 2018/8/21
 */
public class SendCommentActivity extends BaseActivity implements View.OnClickListener,CommentContacts.View {


    private long bookid;
    private long parentid;
    private long replyuserid;

    private EditText etComment;

    private CommentPresenter commentPresenter;


    public static final void lunchActivityForResult(Activity activity,long bookid,long parentid,long replyuserid,int requestCode){
        Intent intent = new Intent(activity,SendCommentActivity.class);
        intent.putExtra("bookid",bookid);
        intent.putExtra("parentid",parentid);
        intent.putExtra("replyuserid",replyuserid);
        activity.startActivityForResult(intent,requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_comment);
        bookid = getIntent().getLongExtra("bookid",bookid);
        parentid = getIntent().getLongExtra("parentid",parentid);
        replyuserid = getIntent().getLongExtra("replyuserid",replyuserid);
        initView();
        commentPresenter = new CommentPresenter();
        commentPresenter.attachView(this);
    }

    private void initView() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.send_comment);

        etComment = (EditText) findViewById(R.id.etComment);
        findViewById(R.id.btnSend).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ivPublicBack){
            finish();
        }else if(view.getId() == R.id.btnSend){
            String comment = etComment.getText().toString();
            if(TextUtils.isEmpty(comment)){
                showToast(getString(R.string.comment_empty));
            }else{
                sendComment(comment);
            }
        }
    }

    private void sendComment(String comment) {
        int userid = UserInfoManager.getInstance(mContext).getUserid();
        commentPresenter.sendComment(userid,bookid,0,0,comment);
    }

    @Override
    public void getBookCommentSuccess(BookDetailCommentResponse bookDetailCommentResponse) {

    }

    @Override
    public void getBookCommentByTypeSuccess(BookCommentByTypeResponse bookCommentByTypeResponse) {

    }

    @Override
    public void getCommentReplyListSuccess(CommentReplyResponse commentReplyResponse) {

    }

    @Override
    public void sendCommentSuccess(SendCommentResponse sendCommentResponse) {
        showToast(getString(R.string.send_comment_success));
        Intent intent = new Intent();
        intent.putExtra("receive",sendCommentResponse.receive);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void sendLikeSuccess() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commentPresenter.detachView();
    }
}
