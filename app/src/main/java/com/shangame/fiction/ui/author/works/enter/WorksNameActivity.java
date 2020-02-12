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

public class WorksNameActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEditName;
    private TextView mTextCount;
    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_name);

        initView();
        initListener();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        mEditName = findViewById(R.id.edit_name);
        mTextCount = findViewById(R.id.text_count);
        mBtnSubmit = findViewById(R.id.btn_submit);

        String name = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(name)) {
            mEditName.setText(name);
            if (name.length() > 0) {
                mBtnSubmit.setEnabled(true);
            } else {
                mBtnSubmit.setEnabled(false);
            }
        }

        tvTitle.setText("作品名称");
    }

    private void initListener() {
        mBtnSubmit.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
        mEditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = mEditName.getText().toString().length();
                if (length > 0) {
                    mBtnSubmit.setEnabled(true);
                } else {
                    mBtnSubmit.setEnabled(false);
                }
                String lenStr = length + "/ 15";
                mTextCount.setText(lenStr);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit: {
                String name = mEditName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    Intent intent = new Intent();
                    intent.putExtra("name", name);
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
