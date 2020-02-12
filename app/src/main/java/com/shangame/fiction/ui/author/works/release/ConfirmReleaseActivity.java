package com.shangame.fiction.ui.author.works.release;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.net.response.AddChapterResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.widget.DataCallBack;
import com.shangame.fiction.widget.DateTimePickerFragment;

import java.util.HashMap;
import java.util.Map;

public class ConfirmReleaseActivity extends BaseActivity implements View.OnClickListener, ConfirmReleaseContacts.View, DataCallBack {

    private ImageView mImgSwitch;
    private TextView mTextReleaseTime;
    private View mViewTimedRelease;
    private ConstraintLayout mLayoutReleaseTime;

    private ConfirmReleasePresenter mPresenter;

    private boolean isTimedRelease = false;

    private int mBookId;
    private String mAuthorWorks;
    private String mTitle;
    private String mContent;
    private String mReleaseTime = "";
    private int mCid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_release);

        initView();
        initListener();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new ConfirmReleasePresenter();
        mPresenter.attachView(this);
    }

    private void initView() {
        TextView textName = findViewById(R.id.text_name);
        TextView textChapterName = findViewById(R.id.text_chapter_name);
        TextView textCount = findViewById(R.id.text_count);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvAuthorWorks = findViewById(R.id.text_author_words);
        mImgSwitch = findViewById(R.id.img_switch);
        mTextReleaseTime = findViewById(R.id.text_release_time);

        mViewTimedRelease = findViewById(R.id.view_timed_release);
        mLayoutReleaseTime = findViewById(R.id.layout_release_time);

        tvTitle.setText("确认发布");

        mBookId = getIntent().getIntExtra("bookId", 0);

        mTitle = getIntent().getStringExtra("title");
        mContent = getIntent().getStringExtra("content");
        mAuthorWorks = getIntent().getStringExtra("authorWorks");
        if (TextUtils.isEmpty(mAuthorWorks)) {
            mAuthorWorks = "";
        }
        if (!TextUtils.isEmpty(mAuthorWorks)) {
            tvAuthorWorks.setText("已填写");
        } else {
            tvAuthorWorks.setText("未填写");
        }

        mCid = getIntent().getIntExtra("cid", 0);

        String bookName = getIntent().getStringExtra("bookName");
        textName.setText(bookName);

        textChapterName.setText(mTitle);
        if (!TextUtils.isEmpty(mContent)) {
            String strCount = mContent.length() + "字";
            textCount.setText(strCount);
        }
    }

    private void initListener() {
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.btn_release).setOnClickListener(this);
        mImgSwitch.setOnClickListener(this);
        mTextReleaseTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_release:
                if (isTimedRelease) {
                    if (!TextUtils.isEmpty(mReleaseTime)) {
                        addChapter();
                    } else {
                        Toast.makeText(mContext, "请填写发布时间！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    addChapter();
                }
                break;
            case R.id.img_switch: {
                isTimedRelease = !isTimedRelease;
                if (isTimedRelease) {
                    mImgSwitch.setImageResource(R.drawable.icon_switch_on);
                    mViewTimedRelease.setVisibility(View.VISIBLE);
                    mLayoutReleaseTime.setVisibility(View.VISIBLE);
                } else {
                    mReleaseTime = "";
                    mTextReleaseTime.setText("");
                    mImgSwitch.setImageResource(R.drawable.icon_switch_off);
                    mViewTimedRelease.setVisibility(View.GONE);
                    mLayoutReleaseTime.setVisibility(View.GONE);
                }
            }
            break;
            case R.id.text_release_time:
                setReleaseTime();
                break;
            default:
                break;
        }
    }

    private void setReleaseTime() {
        // showTimePickerDialog();
        //实例化对象
        DateTimePickerFragment dateTimePickerFragment = new DateTimePickerFragment();
        //调用show方法弹出对话框
        // 第一个参数为FragmentManager对象
        // 第二个为调用该方法的fragment的标签
        dateTimePickerFragment.show(getSupportFragmentManager(), "date_picker");
    }

    private void addChapter() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", UserInfoManager.getInstance(this).getUserid());
        map.put("cid", mCid);
        map.put("bookid", mBookId);
        map.put("title", mTitle);
        map.put("text", mContent);
        map.put("releasetime", mReleaseTime);
        map.put("drafts", 0);
        map.put("authortext", mAuthorWorks);
        mPresenter.addChapter(map);
    }

    @Override
    public void addChapterSuccess(AddChapterResponse bean) {
        Toast.makeText(this, "发布成功！", Toast.LENGTH_SHORT).show();
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastAction.RELEASE_CHAPTER_COMPLETE));
        finish();
    }

    @Override
    public void addChapterFailure(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "发布失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void getTime(String time) {
        Log.e("hhh", "time= " + time);
        mReleaseTime = time;
        mTextReleaseTime.setText(time);
    }
}
