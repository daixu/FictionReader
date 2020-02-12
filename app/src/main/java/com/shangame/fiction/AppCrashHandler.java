package com.shangame.fiction;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.shangame.fiction.core.manager.ActivityStack;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.storage.manager.FileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create by Speedy on 2018/7/17
 */
public final class AppCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "AppCrashHandler";

    private Context mContext;

    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static final AppCrashHandler crashHandler = new AppCrashHandler();

    private AppCrashHandler(){}



    // 获取CrashHandler实例 ，单例模式
    public static AppCrashHandler getInstance(){

        return crashHandler;
    }




    public void init(Context context){
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }




    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        Logger.e(TAG, "uncaughtException: ",ex );

        if(!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }else {
            try {
                Thread.sleep(1000);  	//停顿2秒钟
            } catch (InterruptedException e) {

            }


            ActivityStack.popAllActivity();


            //杀掉进程
            android.os.Process.killProcess(android.os.Process.myPid());

        }
    }



    /**
     * 自定义异常处理:收集错误信息&发送错误报告
     * @param ex
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {

        if (ex == null) {
            return false;
        }
        ActivityStack.popAllActivity();
        saveCrashInfo2File(mContext,ex);
        return true;
    }



    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return  返回全文件名称,便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Context context,Throwable ex) {
        try {

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

            String time = formatter.format(new Date());
            String errorFileName = "crash_" + time  + ".log";

            String spath = FileManager.getInstance(context).getCrashDir();
            String logFilepath = new StringBuffer(spath).append("/Crash/").toString();


            File errorFile = new File(logFilepath,errorFileName);
            if (!errorFile.getParentFile().exists()) {
                errorFile.getParentFile().mkdirs();
            }

            errorFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(errorFile);

            final String errorEnfo = getCrashReport(mContext, ex);

            fos.write(errorEnfo.getBytes());
            fos.close();

            return errorEnfo;

        } catch (Throwable e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }

        return null;
    }



    /**
     * 获取APP崩溃异常报告
     * @param ex
     * @return
     */
    private String getCrashReport(Context context, Throwable ex) {

        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(
                    context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


        StringBuffer sb = new StringBuffer();

        sb.append("\n\n");
        sb.append("\n异常时间:"+new Date().toString());
        sb.append("\n操作系统：Android" );

        sb.append("\n念念versionName:	"+mPackageInfo.versionName);
        sb.append("\n念念versionCode:	" + mPackageInfo.versionCode);
        sb.append("\n手机机型MODEL:	"+ Build.MODEL);
        sb.append("\n手机系统版本:	"+ Build.VERSION.RELEASE);
        sb.append("\nSDK_INT:	"+ Build.VERSION.SDK_INT);
        sb.append("\nPRODUCT	" + Build.PRODUCT);
        sb.append("\n\n");
        sb.append("PrintStackTrace: \n\n" + obtainExceptionInfo(ex));

        return sb.toString();
    }

    /**
     * @param throwable
     * @return
     * @方法说明:获取系统未捕捉的错误信息
     * @方法名称:obtainExceptionInfo
     * @返回值:String
     */
    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();
        return mStringWriter.toString();
    }
}
