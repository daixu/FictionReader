package com.shangame.fiction.ui.author.works.setting;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.BookDataBean;
import com.shangame.fiction.net.response.ClassAllFigResponse;
import com.shangame.fiction.net.response.UpLoadImageResponse;
import com.shangame.fiction.storage.manager.FileManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.author.works.enter.WorksNameActivity;
import com.shangame.fiction.ui.author.works.enter.WorksSynopsisActivity;
import com.shangame.fiction.ui.author.works.type.WorksTypeActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WorksSettingActivity extends BaseActivity implements View.OnClickListener, WorksSettingContacts.View {
    private int mBookId;
    private ImageView mImageCover;
    private TextView mTextName;
    private TextView mTextSynopsis;
    private TextView mTextStatus;
    private TextView mTextType;
    private TextView mTextAuthorizationType;

    private int mSex;
    private int mStatus;

    private String mBookName;
    private String mSynopsis;
    private int mSuperClassId;
    private int mSubClassId;
    private String mBookCover;

    private WorksSettingPresenter mPresenter;

    private static final int NAME_CODE = 101;
    private static final int SYNOPSIS_CODE = 102;
    private static final int TYPE_CODE = 103;

    // 相册选择回传码
    public final static int ALBUM_REQUEST_CODE = 104;

    // 拍照的照片的存储位置
    private String mTempPhotoPath;
    // 照片所在的Uri地址
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_setting);

        mBookId = getIntent().getIntExtra("bookId", 0);
        Log.e("hhh", "bookId= " + mBookId);

        initView();
        initListener();
        initPresenter();
        initData();
    }

    private void initPresenter() {
        mPresenter = new WorksSettingPresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        mPresenter.getBookData(mBookId);
    }

    private void initListener() {
        findViewById(R.id.tv_modify_works).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
        mImageCover.setOnClickListener(this);
        mTextName.setOnClickListener(this);
        mTextSynopsis.setOnClickListener(this);
        mTextStatus.setOnClickListener(this);
        mTextType.setOnClickListener(this);
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        mImageCover = findViewById(R.id.image_cover);
        mTextName = findViewById(R.id.text_name);
        mTextSynopsis = findViewById(R.id.text_synopsis);
        mTextStatus = findViewById(R.id.text_status);
        mTextType = findViewById(R.id.text_type);
        mTextAuthorizationType = findViewById(R.id.text_authorization_type);

        tvTitle.setText("作品设置");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.image_cover: {
                openAlbum();
            }
            break;
            case R.id.text_name: {
                Intent intent = new Intent(WorksSettingActivity.this, WorksNameActivity.class);
                intent.putExtra("name", mBookName);
                startActivityForResult(intent, NAME_CODE);
            }
            break;
            case R.id.text_synopsis: {
                Intent intent = new Intent(WorksSettingActivity.this, WorksSynopsisActivity.class);
                intent.putExtra("synopsis", mSynopsis);
                startActivityForResult(intent, SYNOPSIS_CODE);
            }
            break;
            case R.id.text_status: {
                authorStatus();
            }
            break;
            case R.id.text_type: {
                Intent intent = new Intent(WorksSettingActivity.this, WorksTypeActivity.class);
                intent.putExtra("sex", mSex);
                startActivityForResult(intent, TYPE_CODE);
            }
            break;
            case R.id.tv_modify_works:
                Log.e("hhh", "sex= " + mSex);
                if (verify()) {
                    modifyWorks();
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
        return true;
    }

    private void modifyWorks() {
        long userId = UserInfoManager.getInstance(WorksSettingActivity.this).getUserid();
        Map<String, Object> map = new HashMap<>();
        map.put("bookid", mBookId);
        map.put("bookname", mBookName);
        map.put("bookcover", mBookCover);
        map.put("superclassId", mSuperClassId);
        map.put("subclassId", mSubClassId);
        map.put("synopsis", mSynopsis);
        map.put("userid", userId);
        map.put("keyword", "");
        map.put("status", mStatus);
        mPresenter.updateBook(map);
    }

    private void authorStatus() {
        final String[] items = {"连载中", "已完结"};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("连载状态")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextStatus.setText(items[which]);
                        mStatus = which;
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void openAlbum() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ALBUM_REQUEST_CODE);
        } else {
            try {
                lunchOpenAlbumActivity();
            } catch (Exception e) {
                showToast(getString(R.string.open_album_failed));
                e.printStackTrace();
            }
        }
    }

    private void lunchOpenAlbumActivity() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, ALBUM_REQUEST_CODE);
    }

    @Override
    public void getBookDataSuccess(BookDataBean bean) {
        mBookCover = bean.bookcover;

        Glide.with(mContext).load(bean.bookcover).into(mImageCover);

        mBookName = bean.bookname;
        mTextName.setText(mBookName);

        mSynopsis = bean.synopsis;
        mTextSynopsis.setText(mSynopsis);

        String status = bean.status == 0 ? "连载中" : "已完结";
        mStatus = bean.status;
        mTextStatus.setText(status);

        String authorSign = bean.authorsign == 0 ? "驻站作品" : "独家首发";
        mTextAuthorizationType.setText(authorSign);

        mSex = bean.malechannel;

        mSuperClassId = bean.superclassId;
        mSubClassId = bean.subclassId;

        String superName = bean.supername;
        String subName = bean.subname;
        String typeName = "";
        if (!TextUtils.isEmpty(superName)) {
            typeName = superName + "-";
        }
        if (!TextUtils.isEmpty(subName)) {
            typeName = typeName + subName;
        }
        mTextType.setText(typeName);
    }

    @Override
    public void getBookDataFailure(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateBookSuccess() {
        Toast.makeText(this, "修改作品成功！", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void updateBookFailure(String msg) {

    }

    @Override
    public void uploadCoverSuccess(UpLoadImageResponse response) {
        showToast(getString(R.string.modify_success));

        mBookCover = response.headimgurl;
        Log.e("hhh", "bookCover= " + mBookCover);
        RequestOptions options = new RequestOptions()
                .override(100, 128);
        Glide.with(mActivity)
                .asBitmap()
                .load(mBookCover)
                .apply(options)
                .into(mImageCover);
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
                case ALBUM_REQUEST_CODE: {
                    if (null != data) {
                        handleAlbumResult(data);
                    }
                }
                break;
                default:
                    break;
            }
        }
    }

    private void handleAlbumResult(Intent data) {
        try {
            imageUri = data.getData();
            if (imageUri != null) {
                String path = getUrlPath(imageUri);
                String compressPath = FileManager.getInstance(mContext).getCompressDir();
                new Compressor(this)
                        .setDestinationDirectoryPath(compressPath)
                        .setMaxHeight(132)
                        .setMaxWidth(132)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFileAsFlowable(new File(path))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<File>() {
                            @Override
                            public void accept(File file) {
                                mPresenter.uploadCover(mBookId, file.getAbsolutePath());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                throwable.printStackTrace();
                                showError(throwable);
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrlPath(Uri uri) {
        String url = "";
        if (!TextUtils.isEmpty(uri.getAuthority())) {
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (null == cursor) {
                    showToast(getString(R.string.get_image_failed));
                    return url;
                }
                cursor.moveToFirst();
                url = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else {
            url = uri.getPath();
        }
        return url;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
