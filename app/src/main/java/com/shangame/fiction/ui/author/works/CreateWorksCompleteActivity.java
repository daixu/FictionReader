package com.shangame.fiction.ui.author.works;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.net.response.BookDataBean;
import com.shangame.fiction.net.response.ClassAllFigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.author.works.enter.WorksNameActivity;
import com.shangame.fiction.ui.author.works.enter.WorksSynopsisActivity;
import com.shangame.fiction.ui.author.works.type.WorksTypeActivity;

import java.util.HashMap;
import java.util.Map;

public class CreateWorksCompleteActivity extends BaseActivity implements View.OnClickListener, CreateWorksContacts.View {

    private ImageView mImageCover;
    private TextView mTextName;
    private TextView mTextSynopsis;
    private TextView mTextType;
    private TextView mTextAuthorizationType;

    private int mSex;
    private String mBookName;
    private String mSynopsis;
    private int mSuperClassId;
    private int mSubClassId;
    private String mBookCover;
    private int mAuthorSign = -1;

    private CreateWorksPresenter mPresenter;

    private static final int NAME_CODE = 101;
    private static final int SYNOPSIS_CODE = 102;
    private static final int TYPE_CODE = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_works_complete);

        initPresenter();
        initView();
        initListener();

        mSex = getIntent().getIntExtra("sex", 0);
    }

    private void initPresenter() {
        mPresenter = new CreateWorksPresenter();
        mPresenter.attachView(this);
    }

    private void initListener() {
        mImageCover.setOnClickListener(this);
        mTextName.setOnClickListener(this);
        mTextSynopsis.setOnClickListener(this);
        mTextType.setOnClickListener(this);
        mTextAuthorizationType.setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.image_cover).setOnClickListener(this);
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        mImageCover = findViewById(R.id.image_cover);
        mTextName = findViewById(R.id.text_name);
        mTextSynopsis = findViewById(R.id.text_synopsis);
        mTextType = findViewById(R.id.text_type);
        mTextAuthorizationType = findViewById(R.id.text_authorization_type);

        tvTitle.setText("创建作品");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.image_cover:
                break;
            case R.id.text_name: {
                Intent intent = new Intent(CreateWorksCompleteActivity.this, WorksNameActivity.class);
                if (!TextUtils.isEmpty(mBookName)) {
                    intent.putExtra("name", mBookName);
                }
                startActivityForResult(intent, NAME_CODE);
            }
            break;
            case R.id.text_synopsis: {
                Intent intent = new Intent(CreateWorksCompleteActivity.this, WorksSynopsisActivity.class);
                if (!TextUtils.isEmpty(mSynopsis)) {
                    intent.putExtra("synopsis", mSynopsis);
                }
                startActivityForResult(intent, SYNOPSIS_CODE);
            }
            break;
            case R.id.text_type: {
                Intent intent = new Intent(CreateWorksCompleteActivity.this, WorksTypeActivity.class);
                intent.putExtra("sex", mSex);
                startActivityForResult(intent, TYPE_CODE);
            }
            break;
            case R.id.text_authorization_type: {
                authorType();
            }
            break;
            case R.id.btn_submit: {
                Log.e("hhh", "sex= " + mSex);
                if (verify()) {
                    submit();
                }
            }
            break;
            default:
                break;
        }
    }

    private boolean verify() {
        if (TextUtils.isEmpty(mBookName)) {
            Toast.makeText(mContext, "请输入作品名称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mSynopsis)) {
            Toast.makeText(mContext, "请输入作品简介", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mSuperClassId == 0 || mSubClassId == 0) {
            Toast.makeText(mContext, "请选择作品类型", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mAuthorSign == -1) {
            Toast.makeText(mContext, "请选择授权类型", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void authorType() {
        final String[] items = {"驻站作品", "独家首发"};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("授权类型")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextAuthorizationType.setText(items[which]);
                        mAuthorSign = which;
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void submit() {
        long userId = UserInfoManager.getInstance(CreateWorksCompleteActivity.this).getUserid();
        Map<String, Object> map = new HashMap<>();
        map.put("bookname", mBookName);
        map.put("bookcover", mBookCover);
        map.put("superclassId", mSuperClassId);
        map.put("subclassId", mSubClassId);
        map.put("authorsign", mAuthorSign);
        map.put("malechannel", mSex);
        map.put("synopsis", mSynopsis);
        map.put("userid", userId);
        map.put("keyword", "");
        mPresenter.addBook(map);
    }

    @Override
    public void addBookSuccess(BookDataBean bean) {
        Toast.makeText(this, "创建作品成功！", Toast.LENGTH_SHORT).show();
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastAction.CREATE_WORKS_COMPLETE));
        finish();
    }

    @Override
    public void addBookFailure(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NAME_CODE: {
                    if (null != data) {
                        mBookName = data.getStringExtra("name");
                        Log.e("hhh", "name= " + mBookName);
                        mTextName.setText(mBookName);
                    }
                }
                break;
                case SYNOPSIS_CODE: {
                    if (null != data) {
                        mSynopsis = data.getStringExtra("synopsis");
                        Log.e("hhh", "synopsis= " + mSynopsis);
                        mTextSynopsis.setText(mSynopsis);
                    }
                }

                break;
                case TYPE_CODE: {
                    if (null != data) {
                        ClassAllFigResponse.SuperDataBean superBean = data.getParcelableExtra("superBean");
                        ClassAllFigResponse.SuperDataBean.SubDataBean subBean = data.getParcelableExtra("subBean");
                        mSuperClassId = superBean.classid;
                        mSubClassId = subBean.classid;
                        mBookCover = superBean.bookcover;

                        String supName = superBean.classname;
                        String subName = subBean.classname;

                        String type = supName + "-" + subName;
                        mTextType.setText(type);

                        Glide.with(mContext).load(mBookCover).into(mImageCover);
                    }
                }
                break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
