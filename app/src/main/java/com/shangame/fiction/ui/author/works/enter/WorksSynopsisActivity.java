package com.shangame.fiction.ui.author.works.enter;

import android.content.Intent;
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

public class WorksSynopsisActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEditSynopsis;
    private TextView mTextCount;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_synopsis);

        initView();
        initListener();
    }

    private void initListener() {
        findViewById(R.id.img_back).setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        mEditSynopsis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = mEditSynopsis.getText().toString().length();
                if (length > 20) {
                    mBtnSubmit.setEnabled(true);
                } else {
                    mBtnSubmit.setEnabled(false);
                }
                String lenStr = length + "/ 500";
                mTextCount.setText(lenStr);
            }
        });
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        mEditSynopsis = findViewById(R.id.edit_synopsis);
        mTextCount = findViewById(R.id.text_count);
        mBtnSubmit = findViewById(R.id.btn_submit);

        String synopsis = getIntent().getStringExtra("synopsis");
        if (!TextUtils.isEmpty(synopsis)) {
            mEditSynopsis.setText(synopsis);
            if (synopsis.length() > 20) {
                mBtnSubmit.setEnabled(true);
            } else {
                mBtnSubmit.setEnabled(false);
            }
        }

        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
            mEditSynopsis.setHint("请输入内容，20-500字");
        } else {
            tvTitle.setText("作品简介");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit: {
                String synopsis = mEditSynopsis.getText().toString().trim();
                if (!TextUtils.isEmpty(synopsis)) {
                    Intent intent = new Intent();
                    intent.putExtra("synopsis", synopsis);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
            break;
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }
}
