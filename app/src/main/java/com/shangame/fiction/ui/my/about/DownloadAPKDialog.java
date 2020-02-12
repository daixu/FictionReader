package com.shangame.fiction.ui.my.about;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.core.utils.FileUtils;
import com.shangame.fiction.storage.manager.FileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 下载APK 对话框
 * Created by Speedy. on 2016/11/3.
 */

public class DownloadAPKDialog extends DialogFragment {

    private String url;
    private String fileName;

    private ProgressDialog mProgressDialog;
    private Activity mActivity;

    private DownloadLinstener downloadLinstener;

    public static DownloadAPKDialog newIntance(String url,String fileName){
        DownloadAPKDialog  diglog = new DownloadAPKDialog();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bundle.putString("fileName",fileName);
        diglog.setArguments(bundle);
        return diglog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        if(getArguments() != null){
            url = getArguments().getString("url");
            fileName = getArguments().getString("fileName");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle(R.string.downloading_new_version);
        // 监听Key事件被传递给dialog
        mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                return true;
            }
        });
        return mProgressDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(url != null){
            DownloadAPKTask downloadAPKTask = new DownloadAPKTask(url,fileName);
            downloadAPKTask.execute();
        }
    }

    /**
     * 下载最新APK任务
     *
     * @author shizheng
     *
     */
    private class DownloadAPKTask extends AsyncTask<Void, Integer, Boolean> {

        private static final String TAG = "DownloadAPKTask";

        private String downloadUrl;

        private String apkName; // apk名称

        private long apkSize; // APK的大小

        private long downloadSize; // 已经下载了的大小

        private String savePath; // APK存储路径

        private FileOutputStream outputStream;

        private File apkFile;

        private boolean initialize = false;



        /**
         *
         * @param downloadUrl
         *            下载对应的URL
         * @param apkName
         *            apk名称 （备注：需带后缀名）
         */
        public DownloadAPKTask(String downloadUrl, String apkName) {
            super();
            this.downloadUrl = downloadUrl;
            this.apkName = apkName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {

                    savePath = FileManager.getInstance(mActivity.getApplicationContext()).getCacheDir()+ File.separator+apkName;

                    apkFile = new File(savePath);
                    // 删除历史APK
                    if (apkFile.exists()) {
                        apkFile.delete();
                    }
                    apkFile.createNewFile();
                    outputStream = new FileOutputStream(apkFile);

                if (outputStream == null) {
                    Logger.e(TAG, "outputStream == null");
                    initialize = false;
                }
                initialize = true;
            } catch (Exception e) {
                initialize = false;
                Logger.e(TAG, "下载" + apkName + "初始化错误");
                e.printStackTrace();
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (!initialize) {
                return false;
            }

            Log.i(TAG, "开始下载  " + apkName + "应用");
            try {
                long downloadSize = downloadAPK();
                return downloadSize == apkSize;
            } catch (Exception e) {
                Log.e(TAG, "下载出现" + apkName + "出现异常 ");
            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(mProgressDialog != null && mProgressDialog.isShowing()){
                try{
                    mProgressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


            if (result) {
                File file = new File(savePath);
                if (downloadLinstener != null) {
                    downloadLinstener.onFinish(file);
                }
            }
//
//                try{
//                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
//                        try{
//
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            Uri contentUri = FileProvider.getUriForFile(mActivity,"com.shangame.fiction.my.provider",file);
//                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
//                            startActivity(intent);
//                        }catch (Exception e){
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//                            startActivity(intent);
//                        }
//                    }else{
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//                        startActivity(intent);
//                    }
//                }catch (Exception e){
//                    Toast.makeText(mActivity,"获取安装权限失败，请前往应用商城下载最近版本",Toast.LENGTH_LONG).show();
//                    FileUtils.openAssignFolder(mActivity,savePath);
//                }
//
//            }else{
//                Toast.makeText(mActivity,"下载失败，请前往应用商城下载最近版本",Toast.LENGTH_LONG).show();
//            }
        }






        /**
         * 更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            if(values[0]!= -1){
                try{
                    if(mProgressDialog.isShowing()){
                        mProgressDialog.setMessage(getString(R.string.downloaded_progress,values[0] + "%"));
                        mProgressDialog.setProgress(values[0].intValue());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        private long downloadAPK() throws Exception {

            HttpURLConnection httpConnection = null;
            InputStream is = null;

            try {
                Log.i(TAG, "downloadAPK: "+downloadUrl);
                URL url = new URL(downloadUrl);

                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
                httpConnection.setRequestProperty("Accept-Encoding", "identity");
                if (downloadSize > 0) {
                    httpConnection.setRequestProperty("RANGE", "bytes=" + downloadSize + "-");
                }
                httpConnection.setConnectTimeout(10000);
                httpConnection.setReadTimeout(20000);

                apkSize = httpConnection.getContentLength();

                if (httpConnection.getResponseCode() == 404) {
                    throw new Exception("fail!");
                }
                is = httpConnection.getInputStream();
                byte buffer[] = new byte[1024 * 5]; // 5Kb的缓存
                int readLength = 0;
                int progress = 0;
                int temp = 0;
                while ((readLength = is.read(buffer)) > 0) {

                    outputStream.write(buffer, 0, readLength);
                    downloadSize += readLength;
                    progress = (int) (downloadSize * 100 / apkSize); // 进度百分比

//					Log.i(TAG, apkName + " 已经加载:" + progress + "% apkSize = " + apkSize + " downloadSize = " + downloadSize);

                    if (progress >= temp || progress == 100) {
                        temp += 3;
                        publishProgress(progress); // 每下载超过百分之三更新一下进度，防止更新过于频繁
                    }

                }

                buffer = null; // 释放缓存内存资源

            } catch (Exception e) {

                e.printStackTrace();
                publishProgress(-1);
                throw e;
            } finally {

                if (httpConnection != null) {
                    httpConnection.disconnect();
                    httpConnection = null;
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                }
            }

            if (apkFile.exists() && apkFile.length() == apkSize) {

                Log.i(TAG, apkName + "下载完毕");
                return apkSize;

            } else {
                publishProgress(-1); // 提示下载失败
                return -1;
            }
        }


    }


    public void setDownloadLinstener(DownloadLinstener downloadLinstener) {
        this.downloadLinstener = downloadLinstener;
    }

    public interface DownloadLinstener{
        void onFinish(File file);
    }


}

