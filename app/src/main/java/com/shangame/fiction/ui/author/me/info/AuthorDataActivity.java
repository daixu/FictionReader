package com.shangame.fiction.ui.author.me.info;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.AuthorInfoResponse;
import com.shangame.fiction.net.response.UpLoadImageResponse;
import com.shangame.fiction.storage.manager.FileManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.author.works.enter.EditInfoActivity;
import com.shangame.fiction.ui.common.MenuPopupWindow;
import com.shangame.fiction.ui.setting.personal.UploadContacts;
import com.shangame.fiction.ui.setting.personal.UploadPresenter;
import com.shangame.fiction.widget.GlideApp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AuthorDataActivity extends BaseActivity implements View.OnClickListener, AuthorDataContacts.View, UploadContacts.View {
    // 拍照回传码
    public final static int CAMERA_REQUEST_CODE = 0;
    // 相册选择回传码
    public final static int ALBUM_REQUEST_CODE = 1;
    public final static int SYNOPSIS_CODE = 101;
    private TextView mTextOption;
    private RoundedImageView mImageAvatar;
    private TextView mTextPenName;
    private TextView mTextAuthorId;
    private EditText mEditQq;
    private EditText mEditEmail;
    private TextView mTextSynopsis;
    private AuthorInfoResponse mAuthorInfo;
    private AuthorDataPresenter mPresenter;
    private UploadPresenter mUploadPresenter;
    // 拍照的照片的存储位置
    private String mTempPhotoPath;
    // 照片所在的Uri地址
    private Uri imageUri;

    private String mHeadImgUrl = "";
    private String mSynopsis = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_data);

        initView();
        initListener();
        initPresenter();
    }

    private void initView() {
        mTextOption = findViewById(R.id.text_option);
        mImageAvatar = findViewById(R.id.image_avatar);
        mTextPenName = findViewById(R.id.text_pen_name);
        mTextAuthorId = findViewById(R.id.text_author_id);
        mEditQq = findViewById(R.id.edit_qq);
        mEditEmail = findViewById(R.id.edit_email);
        mTextSynopsis = findViewById(R.id.text_synopsis);

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("个人资料");

        mTextOption.setVisibility(View.VISIBLE);
        mTextOption.setText("保存");

        mAuthorInfo = getIntent().getParcelableExtra("authorInfo");
        if (null != mAuthorInfo) {
            mHeadImgUrl = mAuthorInfo.headimgurl;
            // ImageLoader.with(mActivity).loadUserIcon(mImageAvatar, mAuthorInfo.headimgurl, R.drawable.default_head);
            GlideApp.with(mContext)
                    .load(mAuthorInfo.headimgurl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.default_head)
                    .into(mImageAvatar);

            String penName = mAuthorInfo.penname;
            if (!TextUtils.isEmpty(penName)) {
                mTextPenName.setText(penName);
            }

            int authorId = mAuthorInfo.authorid;
            mTextAuthorId.setText(authorId + "");

            String qq = mAuthorInfo.qq;
            if (!TextUtils.isEmpty(qq)) {
                mEditQq.setText(qq);
            }

            String email = mAuthorInfo.email;
            if (!TextUtils.isEmpty(email)) {
                mEditEmail.setText(email);
            }

            String synopsis = mAuthorInfo.synopsis;
            if (!TextUtils.isEmpty(synopsis)) {
                mTextSynopsis.setText(synopsis);
                mSynopsis = synopsis;
            }
        }
    }

    private void initListener() {
        findViewById(R.id.img_back).setOnClickListener(this);
        mTextOption.setOnClickListener(this);
        mImageAvatar.setOnClickListener(this);
        mTextSynopsis.setOnClickListener(this);
        findViewById(R.id.img_avatar).setOnClickListener(this);
    }

    private void initPresenter() {
        mPresenter = new AuthorDataPresenter();
        mPresenter.attachView(this);

        mUploadPresenter = new UploadPresenter();
        mUploadPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mUploadPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back: {
                finish();
            }
            break;
            case R.id.text_option: {
                saveInfo();
            }
            break;
            case R.id.img_avatar:
            case R.id.image_avatar: {
                List<String> itemList = new ArrayList<>();
                itemList.add("拍照");
                itemList.add("从手机相册选择");
                final MenuPopupWindow menuPopupWindow = new MenuPopupWindow(mActivity, itemList);
                menuPopupWindow.setOnItemClickListener(new MenuPopupWindow.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        menuPopupWindow.dismiss();
                        if (position == 0) {
                            takePhoto();
                        } else {
                            openAlbum();
                        }
                    }
                });
                menuPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            }
            break;
            case R.id.text_synopsis: {
                Intent intent = new Intent(mContext, EditInfoActivity.class);
                intent.putExtra("title", "自我介绍");
                String bankAccountName = mTextSynopsis.getText().toString().trim();
                if (!TextUtils.isEmpty(bankAccountName)) {
                    intent.putExtra("content", bankAccountName);
                }
                intent.putExtra("hint", "用几句话介绍自己吧~.");
                startActivityForResult(intent, SYNOPSIS_CODE);
            }
            break;
            default:
                break;
        }
    }

    private void saveInfo() {
        Map<String, Object> map = new HashMap<>();
        String qq = mEditQq.getText().toString();
        String email = mEditEmail.getText().toString();
        if (!TextUtils.isEmpty(qq)) {
            Toast.makeText(mContext, "请输入QQ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.contains("@")) {
            Toast.makeText(mContext, "请输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
            return;
        }
        map.put("userid", UserInfoManager.getInstance(mContext).getUserid());
        map.put("synopsis", mSynopsis);
        map.put("email", email);
        map.put("qq", qq);
        map.put("headimgurl", mHeadImgUrl);
        mPresenter.updateAuthorInfo(map);
    }

    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mActivity,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    },
                    CAMERA_REQUEST_CODE);
        } else {
            try {
                lunchTakePhotoActivity();
            } catch (Exception e) {
                showToast(getString(R.string.take_photo_failed));
                e.printStackTrace();
            }
        }
    }

    private void openAlbum() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ALBUM_REQUEST_CODE);
        } else {
            try {
                lunchOpenAlbumActivity();
            } catch (Exception e) {
                showToast(getString(R.string.open_album_failed));
                e.printStackTrace();
            }
        }
    }

    private void lunchTakePhotoActivity() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mTempPhotoPath = FileManager.getInstance(mContext).getCacheDir() + File.separator + "temp.jpeg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".my.provider", new File(mTempPhotoPath));
        } else {
            imageUri = Uri.fromFile(new File(mTempPhotoPath));
        }
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, CAMERA_REQUEST_CODE);
    }

    private void lunchOpenAlbumActivity() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, ALBUM_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                handleCameraResult(resultCode);
                break;
            case ALBUM_REQUEST_CODE:
                handleAlbumResult(resultCode, data);
                break;
            case SYNOPSIS_CODE:
                handleSynopsisResult(resultCode, data);
                break;
            default:
                break;
        }
    }

    private void handleCameraResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            String compressPath = FileManager.getInstance(mContext).getCompressDir();
            new Compressor(this)
                    .setDestinationDirectoryPath(compressPath)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setMaxHeight(132)
                    .setMaxWidth(132)
                    .compressToFileAsFlowable(new File(mTempPhotoPath))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            mUploadPresenter.uploadImage(mAuthorInfo.userid, file.getAbsolutePath());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            showError(throwable);
                        }
                    });
        }
    }

    private void handleAlbumResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // 获取图片
            try {
                //该uri是上一个Activity返回的
                if (null != data) {
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
                                        mUploadPresenter.uploadImage(mAuthorInfo.userid, file.getAbsolutePath());
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) {
                                        throwable.printStackTrace();
                                        showError(throwable);
                                    }
                                });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSynopsisResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (null != data) {
                mSynopsis = data.getStringExtra("content");
                if (!TextUtils.isEmpty(mSynopsis)) {
                    mTextSynopsis.setText(mSynopsis);
                }
            }
        }
    }

    private String getUrlPath(Uri uri) {
        String url = "";
        if (!TextUtils.isEmpty(uri.getAuthority())) {
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri,
                        new String[]{MediaStore.Images.Media.DATA}, null, null, null);
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
    public void updateAuthorInfoSuccess() {
        Toast.makeText(this, "修改成功!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void updateAuthorInfoFailure(String msg) {
        Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadImageSuccess(UpLoadImageResponse response) {
        showToast(getString(R.string.modify_success));

        mHeadImgUrl = response.headimgurl;

        RequestOptions options = new RequestOptions()
                .circleCrop()
                .override(105, 105);
        Glide.with(mActivity)
                .asBitmap()
                .load(response.headimgurl)
                .apply(options)
                .into(mImageAvatar);
    }
}
