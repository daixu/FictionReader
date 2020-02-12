package com.shangame.fiction.ui.share.poster;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.utils.BitmapUtils;
import com.shangame.fiction.core.utils.DensityUtil;
import com.shangame.fiction.core.utils.QrEncodeUtils;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.storage.manager.FileManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Poster5Fragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private String imagePath;
    private ConstraintLayout mLayoutContent1;
    private ProgressDialog mProgressDialog;

    public Poster5Fragment() {
    }

    public static Poster5Fragment newInstance(int param1) {
        Poster5Fragment fragment = new Poster5Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_poster5, container, false);
        initView(contentView);
        imagePath = FileManager.getInstance(mContext).getCacheDir() + File.separator + "share5.jpeg";
        return contentView;
    }

    private void initView(View contentView) {
        mLayoutContent1 = contentView.findViewById(R.id.layout_content_1);
        ImageView imageQr = contentView.findViewById(R.id.image_qr);

        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();

        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        String url = "http://webapi.anmaa.com/api/agent/set-QrCodeBind?agentid=" + userInfo.agentId + "&channel=" + ApiConstant.Channel.ANDROID;
        Bitmap qrBitmap = QrEncodeUtils.createImage(url, DensityUtil.dip2px(mContext, 60), DensityUtil.dip2px(mContext, 60), logo);
        imageQr.setImageBitmap(qrBitmap);
    }

    public void downloadImage(final View.OnClickListener onClickListener) {
        String msg = "正在生成图片……";
        mProgressDialog = ProgressDialog.show(mContext, null, msg, true, false);
        mProgressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Horizontal);
        mProgressDialog.setCancelable(false);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(new File(imagePath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapUtils.loadBitmapFromView(mLayoutContent1);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                bitmap.recycle();
                return null;
            }

            @Override
            protected void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mProgressDialog.dismiss();
                onClickListener.onClick(null);
            }
        }.execute();
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}