package com.shangame.fiction.ui.setting.feedback;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.storage.manager.UserInfoManager;

/**
 * 意见反馈
 * Create by Speedy on 2018/8/7
 */
public class SuggestFeedbackActivity extends BaseActivity implements View.OnClickListener ,FeedbackContacts.View{

    private EditText etFeedbackContent;

    private FeedbackPresenter feedbackPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_feedback);
        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.suggest_feedback);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.btnCommint).setOnClickListener(this);

        etFeedbackContent = (EditText) findViewById(R.id.etFeedbackContent);

        feedbackPresenter = new FeedbackPresenter();
        feedbackPresenter.attachView(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ivPublicBack){
            finish();
        }else if(view.getId() == R.id.btnCommint){
           String msg = etFeedbackContent.getText().toString();
           if(TextUtils.isEmpty(msg)){
               showToast(getString(R.string.feedback_empty));
           }else{
               int userid = UserInfoManager.getInstance(mContext).getUserid();
               feedbackPresenter.feedback(userid,msg);
           }
        }
    }

    @Override
    public void feedbackSuccess() {
        showToast(getString(R.string.feedback_success));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        feedbackPresenter.detachView();
    }
}
