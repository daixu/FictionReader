package com.shangame.fiction.ui.author.works.enter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;

public class EditBandCardActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImgBack;
    private EditText mEditInfo;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_band_card);

        initView();
        initListener();
    }

    private void initListener() {
        mImgBack.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);

        mEditInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = mEditInfo.getText().toString().length();
                if (length > 5) {
                    mBtnSubmit.setEnabled(true);
                } else {
                    mBtnSubmit.setEnabled(false);
                }
            }
        });
    }

    private void initView() {
        mImgBack = findViewById(R.id.img_back);
        TextView tvTitle = findViewById(R.id.tv_title);
        mEditInfo = findViewById(R.id.edit_info);
        mBtnSubmit = findViewById(R.id.btn_submit);
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        String content = getIntent().getStringExtra("content");
        if (!TextUtils.isEmpty(content)) {
            mEditInfo.setText(content);

            int length = content.length();
            if (length > 5) {
                mBtnSubmit.setEnabled(true);
            } else {
                mBtnSubmit.setEnabled(false);
            }
        }
        String hint = getIntent().getStringExtra("hint");
        if (!TextUtils.isEmpty(hint)) {
            mEditInfo.setHint(hint);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back: {
                finish();
            }
            break;
            case R.id.btn_submit: {
                String content = mEditInfo.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    Intent intent = new Intent();
                    intent.putExtra("content", content);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(mContext, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                break;
        }
    }
}
