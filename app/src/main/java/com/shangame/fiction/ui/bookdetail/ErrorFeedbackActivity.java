package com.shangame.fiction.ui.bookdetail;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;

public class ErrorFeedbackActivity extends BaseActivity implements View.OnClickListener, ErrorFeedbackContact.View {

    private TextView tvBookName;
    private TextView tvChapterName;
    private TextView tvError1;
    private TextView tvError2;
    private TextView tvError3;
    private TextView tvError4;
    private TextView tvError5;
    private TextView tvError6;
    private TextView tvCount;

    private EditText etExtra;
    private Button btnCommit;

    private ErrorFeedbackPresenter errorFeedbackPresenter;

    private long bookid;
    private long chapterid;
    private String bookName;
    private String chapterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_feedback);
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("章节报错");

        bookid = getIntent().getLongExtra("bookid", 0);
        chapterid = getIntent().getLongExtra("chapterid", 0);
        bookName = getIntent().getStringExtra("bookName");
        chapterName = getIntent().getStringExtra("chapterName");

        tvBookName = findViewById(R.id.tvBookName);
        tvChapterName = findViewById(R.id.tvChapterName);

        tvBookName.setText(bookName);
        tvChapterName.setText(chapterName);

        tvError1 = findViewById(R.id.tvError1);
        tvError2 = findViewById(R.id.tvError2);
        tvError3 = findViewById(R.id.tvError3);
        tvError4 = findViewById(R.id.tvError4);
        tvError5 = findViewById(R.id.tvError5);
        tvError6 = findViewById(R.id.tvError6);
        tvCount = findViewById(R.id.tvCount);
        etExtra = findViewById(R.id.etExtra);
        btnCommit = findViewById(R.id.btnCommit);

        tvError1.setOnClickListener(this);
        tvError2.setOnClickListener(this);
        tvError3.setOnClickListener(this);
        tvError4.setOnClickListener(this);
        tvError5.setOnClickListener(this);
        tvError6.setOnClickListener(this);

        btnCommit.setOnClickListener(this);

        etExtra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!TextUtils.isEmpty(text)) {
                    tvCount.setText(text.length() + "/200");
                } else {
                    tvCount.setText("0/200");
                }
            }
        });

        errorFeedbackPresenter = new ErrorFeedbackPresenter();
        errorFeedbackPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        errorFeedbackPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.tvError1:
                tvError1.setSelected(!tvError1.isSelected());
                break;
            case R.id.tvError2:
                tvError2.setSelected(!tvError2.isSelected());
                break;
            case R.id.tvError3:
                tvError3.setSelected(!tvError3.isSelected());
                break;
            case R.id.tvError4:
                tvError4.setSelected(!tvError4.isSelected());
                break;
            case R.id.tvError5:
                tvError5.setSelected(!tvError5.isSelected());
                break;
            case R.id.tvError6:
                tvError6.setSelected(!tvError6.isSelected());
                break;
            case R.id.btnCommit:
                commit();
                break;
            default:
                break;
        }
    }

    private void commit() {
        StringBuffer sb = new StringBuffer();
        if (tvError1.isSelected()) {
            sb.append(tvError1.getText().toString()).append(",");
        }
        if (tvError2.isSelected()) {
            sb.append(tvError2.getText().toString()).append(",");
        }
        if (tvError3.isSelected()) {
            sb.append(tvError3.getText().toString()).append(",");
        }
        if (tvError4.isSelected()) {
            sb.append(tvError4.getText().toString()).append(",");
        }
        if (tvError5.isSelected()) {
            sb.append(tvError5.getText().toString()).append(",");
        }
        if (tvError6.isSelected()) {
            sb.append(tvError6.getText().toString()).append(",");
        }

        if (sb.length() == 0) {
            showToast("请选择错误类型");
            return;
        }

        String errortype = sb.substring(0, sb.length());

        String remark = etExtra.getText().toString();

        errorFeedbackPresenter.feedbackError(bookid, chapterid, errortype, remark);

    }

    @Override
    public void feedbackErrorSuccess() {
        showToast("提交成功,谢谢您发反馈");
        finish();
    }
}
