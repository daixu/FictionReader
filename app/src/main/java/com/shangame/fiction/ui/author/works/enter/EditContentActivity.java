package com.shangame.fiction.ui.author.works.enter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.net.response.AddChapterResponse;
import com.shangame.fiction.net.response.ChapterResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.author.works.release.ConfirmReleaseActivity;
import com.shangame.fiction.widget.KeyboardStatusDetector;

import java.util.HashMap;
import java.util.Map;

public class EditContentActivity extends BaseActivity implements View.OnClickListener, EditContentContacts.View {

    private TextView mTvTitle;
    private EditText mEditTitle;
    private EditText mEditContent;
    private String mBookName;
    private int mBookId;
    private int mType = -1;
    private String mAuthorWorks = "";
    private int mCid = 0;
    private TextView mTextOption;

    private ImageView mImageAuthorWords;
    private TextView mTextAuthorWords;
    private ConstraintLayout mLayoutBottom;
    private EditContentPresenter mPresenter;

    private static final int AUTHOR_WORKS_CODE = 101;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.RELEASE_CHAPTER_COMPLETE.equals(action)) {
                finish();
            }
        }
    };

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.RELEASE_CHAPTER_COMPLETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);

        initView();
        initListener();
        initReceiver();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new EditContentPresenter();
        mPresenter.attachView(this);
    }

    private void initView() {
        mTvTitle = findViewById(R.id.tv_title);
        mEditTitle = findViewById(R.id.edit_title);
        mEditContent = findViewById(R.id.edit_content);
        mTextOption = findViewById(R.id.text_option);

        mTextOption.setVisibility(View.VISIBLE);

        mImageAuthorWords = findViewById(R.id.image_author_words);
        mTextAuthorWords = findViewById(R.id.text_author_words);
        mLayoutBottom = findViewById(R.id.layout_bottom);

        ChapterResponse.PageDataBean dataBean = getIntent().getParcelableExtra("bean");
        mBookName = getIntent().getStringExtra("bookName");
        mBookId = getIntent().getIntExtra("bookId", 0);
        mType = getIntent().getIntExtra("type", -1);

        if (null != dataBean) {
            String chapterTitle = dataBean.title;
            if (!TextUtils.isEmpty(chapterTitle)) {
                mEditTitle.setText(chapterTitle);
            }
            String chapterContent = dataBean.text;
            if (!TextUtils.isEmpty(chapterContent)) {
                mEditContent.setText(chapterContent);
            }
            String authorWorks = dataBean.authortext;
            if (!TextUtils.isEmpty(authorWorks)) {
                mAuthorWorks = authorWorks;
            }
            int number = dataBean.wordnumber;
            String count = number + "字";
            mTvTitle.setText(count);

            mCid = dataBean.cid;
        } else {
            mTvTitle.setText("0字");
        }
    }

    private void initListener() {
        findViewById(R.id.img_back).setOnClickListener(this);
        mTextOption.setOnClickListener(this);
        mImageAuthorWords.setOnClickListener(this);
        findViewById(R.id.img_delete).setOnClickListener(this);
        findViewById(R.id.img_save).setOnClickListener(this);

        contentListener();
        keyboardListener();
    }

    private void contentListener() {
        mEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = mEditContent.getText().toString().trim();
                String count = content.length() + "字";
                mTvTitle.setText(count);
            }
        });
    }

    private void keyboardListener() {
        KeyboardStatusDetector.setVisibilityListener(this, new KeyboardStatusDetector.KeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean keyboardVisible) {
                if (keyboardVisible) {
                    mImageAuthorWords.setVisibility(View.GONE);
                    mTextAuthorWords.setVisibility(View.GONE);
                    mLayoutBottom.setVisibility(View.GONE);
                } else {
                    mImageAuthorWords.setVisibility(View.VISIBLE);
                    mTextAuthorWords.setVisibility(View.VISIBLE);
                    mLayoutBottom.setVisibility(View.VISIBLE);
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
            case R.id.text_option: {
                releaseChapter();
            }
            break;
            case R.id.image_author_words: {
                Intent intent = new Intent(EditContentActivity.this, WorksSynopsisActivity.class);
                intent.putExtra("title", "作者的话");
                if (!TextUtils.isEmpty(mAuthorWorks)) {
                    intent.putExtra("synopsis", mAuthorWorks);
                }
                startActivityForResult(intent, AUTHOR_WORKS_CODE);
            }
            break;
            case R.id.img_delete:
                if (mCid != 0) {
                    deleteChapter(mCid);
                } else {
                    String title = mEditTitle.getText().toString().trim();
                    String content = mEditContent.getText().toString().trim();
                    if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
                        finish();
                    } else {
                        Toast.makeText(mContext, "该章节尚未保存", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.img_save: {
                saveChapter();
            }
            break;
            default:
                break;
        }
    }

    private void saveChapter() {
        String title = mEditTitle.getText().toString().trim();
        String content = mEditContent.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(mContext, "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userid", UserInfoManager.getInstance(this).getUserid());
        map.put("cid", mCid);
        map.put("bookid", mBookId);
        map.put("title", title);
        map.put("text", content);
        map.put("releasetime", "");
        if (mType == 0) {
            map.put("drafts", 0);
        } else {
            map.put("drafts", 1);
        }
        map.put("authortext", mAuthorWorks);
        mPresenter.saveChapter(map);
    }

    private void deleteChapter(int cid) {
        int volume = 0;
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.deleteChapter(cid, mBookId, volume, userId);
    }

    private void releaseChapter() {
        String title = mEditTitle.getText().toString().trim();
        String content = mEditContent.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(mContext, "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (verify(title, content)) {
            Intent intent = new Intent(EditContentActivity.this, ConfirmReleaseActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            intent.putExtra("bookId", mBookId);
            intent.putExtra("bookName", mBookName);
            intent.putExtra("authorWorks", mAuthorWorks);
            intent.putExtra("cid", mCid);
            startActivity(intent);
        }
    }

    private boolean verify(String title, String content) {
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "请输入章节标题", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入章节内容", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void deleteChapterSuccess() {
        Toast.makeText(mContext, "删除成功!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void deleteChapterFailure(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "删除失败！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void saveChapterSuccess(AddChapterResponse bean) {
        Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
        mCid = bean.cid;
    }

    @Override
    public void saveChapterFailure(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AUTHOR_WORKS_CODE) {
            if (null != data) {
                mAuthorWorks = data.getStringExtra("synopsis");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mPresenter.detachView();
    }
}
