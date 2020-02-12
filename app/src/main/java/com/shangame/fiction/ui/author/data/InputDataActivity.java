package com.shangame.fiction.ui.author.data;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;

public class InputDataActivity extends BaseActivity implements View.OnClickListener {
    private Button mBtnNext;
    private EditText mEditPenName;
    private EditText mEditEmail;
    private EditText mEditQq;

    private boolean hasPenName = false;
    private boolean hasEmail = false;
    private boolean hasQq = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        initView();
    }

    private void initView() {
        mBtnNext = findViewById(R.id.btn_next);
        mEditPenName = findViewById(R.id.edit_pen_name);
        mEditEmail = findViewById(R.id.edit_email);
        mEditQq = findViewById(R.id.edit_qq);

        mBtnNext.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);

        penNameListener();
        emailListener();
        qqListener();
    }

    private void penNameListener() {
        mEditPenName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().length() >= 2) {
                    hasPenName = true;
                }

                if (hasPenName && hasEmail && hasQq) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                }
            }
        });
    }

    private void emailListener() {
        mEditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().length() >= 6) {
                    hasEmail = true;
                }

                if (hasPenName && hasEmail && hasQq) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                }
            }
        });
    }

    private void qqListener() {
        mEditQq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().length() >= 6) {
                    hasQq = true;
                }

                if (hasPenName && hasEmail && hasQq) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_next:
                submit();
                break;
            default:
                break;
        }
    }

    private void submit() {
        String penName = mEditPenName.getText().toString().trim();
        String email = mEditEmail.getText().toString().trim();
        String qq = mEditQq.getText().toString().trim();
        if (TextUtils.isEmpty(penName)) {
            Toast.makeText(mContext, "请输入笔名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.contains("@")) {
            Toast.makeText(mContext, "请输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(qq)) {
            Toast.makeText(mContext, "请输入QQ", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, DetailDataActivity.class);
        intent.putExtra("penName", penName);
        intent.putExtra("email", email);
        intent.putExtra("qq", qq);
        startActivity(intent);
    }
}
