package com.shangame.fiction.ui.setting.personal;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.UpLoadImageResponse;
import com.shangame.fiction.storage.manager.FileManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.common.MenuPopupWindow;
import com.shangame.fiction.ui.setting.personal.area.ProvinceActivity;
import com.shangame.fiction.widget.GlideApp;
import com.ycuwq.datepicker.date.DatePickerDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 个人资料
 * Create by Speedy on 2018/8/7
 */
public class PersonalProfileActivity extends BaseActivity implements View.OnClickListener, PersonalContacts.View, UploadContacts.View {

    // 拍照回传码
    public final static int CAMERA_REQUEST_CODE = 0;
    // 相册选择回传码
    public final static int ALBUM_REQUEST_CODE = 1;

    public final static int MODIFY_NICK_NAME_REQUEST_CODE = 2;
    public final static int MODIFY_INTRO_REQUEST_CODE = 3;
    public final static int MODIFY_AREA_REQUEST_CODE = 4;

    private ImageView ivHeadIcon;
    private TextView tvNickName;
    private TextView tvSex;
    private TextView tvBirthday;
    private TextView tvArea;
    private TextView tvPersonalIntro;

    // 拍照的照片的存储位置
    private String mTempPhotoPath;
    // 照片所在的Uri地址
    private Uri imageUri;

    private PersonalPresenter personalPresenter;

    private UserInfo userInfo;

    private UploadPresenter uploadPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
        initPresenter();
        initView();
        userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        if (userInfo.userid == 0) {
            lunchLoginActivity();
        } else {
            initData(userInfo);
        }
    }

    private void initPresenter() {
        personalPresenter = new PersonalPresenter();
        personalPresenter.attachView(this);

        uploadPresenter = new UploadPresenter();
        uploadPresenter.attachView(this);
    }

    private void initView() {
        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.personal_profile);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.head_layout).setOnClickListener(this);
        findViewById(R.id.nick_name_layout).setOnClickListener(this);
        findViewById(R.id.sexLayout).setOnClickListener(this);
        findViewById(R.id.birthday_layout).setOnClickListener(this);
        findViewById(R.id.area_layout).setOnClickListener(this);
        findViewById(R.id.personal_intro_layout).setOnClickListener(this);

        ivHeadIcon = findViewById(R.id.ivHeadIcon);
        tvNickName = findViewById(R.id.tvNickName);
        tvSex = findViewById(R.id.tvSex);
        tvBirthday = findViewById(R.id.tvBirthday);
        tvArea = findViewById(R.id.tvArea);
        tvPersonalIntro = findViewById(R.id.tvPersonalIntro);
    }

    private void initData(UserInfo userInfo) {
        tvNickName.setText(userInfo.nickname);

        if (userInfo.sex == 0) {
            tvSex.setText(getString(R.string.boy));
        } else {
            tvSex.setText(getString(R.string.girl));
        }
        tvBirthday.setText(userInfo.birthday);
        tvArea.setText(userInfo.province + " " + userInfo.city);
        tvPersonalIntro.setText(userInfo.synopsis);

        // ImageLoader.with(mActivity).loadUserIcon(ivHeadIcon, userInfo.headimgurl);
        GlideApp.with(mActivity)
                .load(userInfo.headimgurl)
                .centerCrop()
                .placeholder(R.drawable.default_head)
                .into(ivHeadIcon);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        personalPresenter.detachView();
        uploadPresenter.detachView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivPublicBack) {
            finish();
        } else if (view.getId() == R.id.head_layout) {
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
            menuPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        } else if (view.getId() == R.id.nick_name_layout) {
            startActivityForResult(new Intent(mActivity, ModifyNickNameActivity.class), MODIFY_NICK_NAME_REQUEST_CODE);
        } else if (view.getId() == R.id.sexLayout) {
            final List<String> itemList = new ArrayList<>();
            itemList.add("男");
            itemList.add("女");
            final MenuPopupWindow menuPopupWindow = new MenuPopupWindow(mActivity, itemList);
            menuPopupWindow.setOnItemClickListener(new MenuPopupWindow.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    menuPopupWindow.dismiss();
                    tvSex.setText(itemList.get(position));
                    userInfo.sex = position;
                    commitModifyProfile();
                }
            });
            menuPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        } else if (view.getId() == R.id.birthday_layout) {
            DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
            datePickerDialogFragment.setOnDateChooseListener(new DatePickerDialogFragment.OnDateChooseListener() {
                @Override
                public void onDateChoose(int year, int month, int day) {
                    String birthday = year + "-" + month + "-" + day;
                    tvBirthday.setText(birthday);
                    userInfo.birthday = birthday;
                    commitModifyProfile();
                }
            });
            datePickerDialogFragment.show(getSupportFragmentManager(), "DatePickerDialogFragment");
        } else if (view.getId() == R.id.area_layout) {
            Intent intent = new Intent(mActivity, ProvinceActivity.class);
            startActivityForResult(intent, MODIFY_AREA_REQUEST_CODE);
        } else if (view.getId() == R.id.personal_intro_layout) {
            startActivityForResult(new Intent(mActivity, ModifyIntroActivity.class), MODIFY_INTRO_REQUEST_CODE);
        }
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

    private void commitModifyProfile() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userInfo.userid);
        map.put("nickname", userInfo.nickname);
        map.put("sex", userInfo.sex);

        if (!TextUtils.isEmpty(userInfo.mobilephone)) {
            map.put("mobilephone", userInfo.mobilephone);
        }
        if (!TextUtils.isEmpty(userInfo.synopsis)) {
            map.put("synopsis", userInfo.synopsis);
        }
        if (!TextUtils.isEmpty(userInfo.province)) {
            map.put("province", userInfo.province);
        }
        if (!TextUtils.isEmpty(userInfo.city)) {
            map.put("city", userInfo.city);
        }
        personalPresenter.modifyProfile(map);
    }

    private void lunchTakePhotoActivity() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mTempPhotoPath = FileManager.getInstance(mContext).getCacheDir() + File.separator + "temp.jpeg";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(mContext,
                    mContext.getApplicationContext().getPackageName() + ".my.provider",
                    new File(mTempPhotoPath));
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
                handleCameraResult(resultCode, data);
                break;
            case ALBUM_REQUEST_CODE:
                handleAlbumResult(resultCode, data);
                break;
            case MODIFY_NICK_NAME_REQUEST_CODE:
                handleModifyNackNameResult(resultCode, data);
                break;
            case MODIFY_INTRO_REQUEST_CODE:
                handleModifyIntroResult(resultCode, data);
            case MODIFY_AREA_REQUEST_CODE:
            case LUNCH_LOGIN_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                    initData(userInfo);
                }
                break;
        }
    }

    private void handleCameraResult(int resultCode, Intent data) {
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
                            uploadPresenter.uploadImage(userInfo.userid, file.getAbsolutePath());
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
                                    uploadPresenter.uploadImage(userInfo.userid, file.getAbsolutePath());
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
    }

    private void handleModifyNackNameResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            initData(userInfo);
        }
    }

    private void handleModifyIntroResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            initData(userInfo);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                lunchTakePhotoActivity();
            } else {
                Toast.makeText(mContext, R.string.take_photo_failed, Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == ALBUM_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lunchOpenAlbumActivity();
            } else {
                Toast.makeText(mContext, R.string.open_album_failed, Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void modifyProfileSuccess(UserInfo userInfo) {
        this.userInfo = userInfo;
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
        showToast(getString(R.string.modify_success));
    }

    @Override
    public void uploadImageSuccess(UpLoadImageResponse upLoadImageResponse) {
        userInfo.headimgurl = upLoadImageResponse.headimgurl;
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

        showToast(getString(R.string.modify_success));

        RequestOptions options = new RequestOptions()
                .circleCrop()
                .override(100, 100);
        Glide.with(mActivity)
                .asBitmap()
                .load(upLoadImageResponse.headimgurl)
                .apply(options)
                .into(ivHeadIcon);
    }
}
