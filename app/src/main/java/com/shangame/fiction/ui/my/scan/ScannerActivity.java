package com.shangame.fiction.ui.my.scan;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.utils.BitmapUtils;
import com.shangame.fiction.core.utils.CodeHints;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

@Route(path = "/ss/my/scanner")
public class ScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler, View.OnClickListener {
    public static final int SELECT_IMAGE_REQUEST_CODE = 301;
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scanner);

        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkRequestPermission();
        }
    }

    private void initView() {
        findViewById(R.id.img_back).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView textOption = findViewById(R.id.text_option);
        tvTitle.setText("二维码");
        textOption.setVisibility(View.VISIBLE);
        textOption.setText("相册");
        textOption.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkRequestPermission() {
        List<String> lackedPermission = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            lackedPermission.add(Manifest.permission.CAMERA);
        }

        // 权限已经有了
        if (lackedPermission.size() == 0) {
            ViewGroup contentFrame = findViewById(R.id.content_frame);
            mScannerView = new ZXingScannerView(this);
            contentFrame.addView(mScannerView);

            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mScannerView) {
            mScannerView.stopCamera();
        }

        Log.e("hhh", "onPause stopCamera");
    }

    @Override
    public void handleResult(Result rawResult) {
//        Toast.makeText(this, "Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("content", rawResult.getText());

        resultIntent.putExtras(bundle);
        setResult(201, resultIntent);
        finish();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
//        Handler handler = new Handler();
//        handler.postDelayed(() -> mScannerView.resumeCameraPreview(ScannerActivity.this), 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (null != data) {
                    handleAlbumPic(data);
                }
            }
        }
    }

    private void handleAlbumPic(Intent data) {
        final Uri uri = data.getData();
        Log.e("hhh", "uri= " + uri);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Result result = scanningImage(uri);
                if (result != null) {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("content", result.getText());

                    resultIntent.putExtras(bundle);
                    setResult(200, resultIntent);
                    finish();
                } else {
                    Toast.makeText(ScannerActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 扫描二维码图片的方法
     *
     * @param uri
     * @return
     */
    private Result scanningImage(Uri uri) {
        if (uri == null) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        Bitmap scanBitmap = BitmapUtils.decodeUri(this, uri, 500, 500);
        int width = scanBitmap.getWidth();
        int height = scanBitmap.getHeight();
        int[] pixels = new int[width * height];
        scanBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(binaryBitmap, CodeHints.getDefaultDecodeHints());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            ViewGroup contentFrame = findViewById(R.id.content_frame);
            mScannerView = new ZXingScannerView(this);
            contentFrame.addView(mScannerView);

            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.text_option:
                chooseImage(v);
                break;
            default:
                break;
        }
    }

    public void chooseImage(View view) {
        Intent innerIntent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            innerIntent.setAction(Intent.ACTION_GET_CONTENT);
        }
        innerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
        startActivityForResult(wrapperIntent, SELECT_IMAGE_REQUEST_CODE);
    }
}
