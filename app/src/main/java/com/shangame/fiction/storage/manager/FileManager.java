package com.shangame.fiction.storage.manager;

import android.content.Context;

import java.io.File;

/**
 * Create by Speedy on 2018/9/6
 */
public final class FileManager {

    private Context mContext;

    private FileManager(Context context){
        mContext = context;
    }

    public static final FileManager getInstance(Context context){
        return new FileManager(context);
    }


    public  final String getRootDir(){
        return mContext.getExternalCacheDir().toString();
    }

    public  final String getCacheDir(){
        String cachePath = getRootDir()+ File.separator+ "cache";
        File file = new File(cachePath);
        if(!file.exists()){
            file.mkdirs();
        }
        return cachePath;
    }

    public  final String getCompressDir(){
        String compressPath = getRootDir()+ File.separator+ "compress";
        File file = new File(compressPath);
        if(!file.exists()){
            file.mkdirs();
        }
        return compressPath;
    }

    public  final String getCrashDir(){
        String compressPath = getRootDir()+ File.separator+ "crash";
        File file = new File(compressPath);
        if(!file.exists()){
            file.mkdirs();
        }
        return compressPath;
    }





}
