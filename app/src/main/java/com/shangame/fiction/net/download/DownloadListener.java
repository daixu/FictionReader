package com.shangame.fiction.net.download;

/**
 * Create by Speedy on 2018/9/17
 */
public interface DownloadListener {

    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload();

    void onFail(String errorInfo);
}
