package com.shangame.fiction.ui.wifi;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.utils.NetworkUtils;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.wifi.nanohttpd.DeviceInfoDispatcher;
import com.shangame.fiction.ui.wifi.nanohttpd.HttpServer;
import com.shangame.fiction.ui.wifi.nanohttpd.LongPollingDispatcher;
import com.shangame.fiction.ui.wifi.nanohttpd.ResourceDispatcher;
import com.shangame.fiction.ui.wifi.nanohttpd.UploadFileDispatcher;
import com.shangame.fiction.ui.wifi.nanohttpd.UploadFileProgressDispathcer;

import org.nanohttpd.protocols.http.progress.ProgressListener;
import org.nanohttpd.protocols.http.request.Method;

import java.io.IOException;

/**
 * WIFI传书activity
 * @author hhh
 */
public class WifiBookActivity extends BaseActivity implements ProgressListener, View.OnClickListener, WifiBookContacts.View {
    private TextView mTvContent;
    private TextView mTvWifiIp;
    private Button mBtnSetting;
    private Button mBtnCopy;
    private TextView mTvStatus;
    private TextView mTvPrompt;

    private HttpServer mHttpServer;
    private long mBytesRead;
    private long mContentLength;
    private String mIp;

    private WifiBookPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_book);
        AppUtils.init(this);

        initBroadcast();
        initServer();
        initView();
        initData();
        initListener();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new WifiBookPresenter();
        mPresenter.attachView(this);
    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkReceiver, filter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.UPLOAD_WIFI_BOOK);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    private void initServer() {
        mHttpServer = new HttpServer(9093, this);
        mHttpServer.register(Method.GET, "/", new ResourceDispatcher(this))
                .register(Method.GET, "/images/.*", new ResourceDispatcher(this))
                .register(Method.GET, "/scripts/.*", new ResourceDispatcher(this))
                .register(Method.GET, "/css/.*", new ResourceDispatcher(this))
                .register(Method.GET, "/imgs/.*", new ResourceDispatcher(this))
                .register(Method.GET, "/js/.*", new ResourceDispatcher(this))
                .register(Method.GET, "/getDeviceInfo", new DeviceInfoDispatcher(this))
                .register(Method.POST, "/upload", new UploadFileDispatcher())
                .register(Method.POST, "/files", new UploadFileDispatcher())
                .register(Method.GET, "/upload", new UploadFileProgressDispathcer(this))
                .register(Method.POST, "/longpolling", new LongPollingDispatcher());
    }

    private void initView() {
        mTvContent = findViewById(R.id.tv_content);
        mTvWifiIp = findViewById(R.id.tv_wifi_ip);
        mBtnSetting = findViewById(R.id.btn_setting);
        mBtnCopy = findViewById(R.id.btn_copy);
        mTvStatus = findViewById(R.id.tv_status);
        mTvPrompt = findViewById(R.id.tv_prompt);
    }

    private void initListener() {
        mBtnSetting.setOnClickListener(this);
        mBtnCopy.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    public void initData() {
        String wifiIp = NetworkUtils.getConnectWifiIp(mContext);
        if (!TextUtils.isEmpty(wifiIp)) {
            mBtnSetting.setVisibility(View.GONE);
            mBtnCopy.setVisibility(View.VISIBLE);
            mTvStatus.setText("WIFI服务已开启");
            mTvPrompt.setText("请确保您的手机和传输设备在同一个局域网内");
            // 启动wifi传书服务器
            try {
                mHttpServer.startServer();
                mIp = "http://".concat(mHttpServer.getHostname()).concat(":").concat(mHttpServer.getListeningPort() + "");
                mTvContent.setText("在电脑浏览器中访问以下地址");
                mTvContent.setTextSize(15);
                mTvWifiIp.setVisibility(View.VISIBLE);
                mTvWifiIp.setText(mIp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mTvStatus.setText("WIFI服务未启动");
            mTvPrompt.setText("请确认您手机设备的连接状态");
            mBtnSetting.setVisibility(View.VISIBLE);
            mBtnCopy.setVisibility(View.GONE);

            mTvContent.setText("没有WIFI信号");
            mTvContent.setTextSize(20);
            mTvWifiIp.setVisibility(View.GONE);
        }
    }

    private void copy(String copyStr) {
        //获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", copyStr);
        // 将ClipData内容放到系统剪贴板里。
        if (null != cm) {
            cm.setPrimaryClip(mClipData);
        }
    }

    private void exitPrompt() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要关闭？Wifi传书将会中断！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    @Override
    public void update(long pBytesRead, long pContentLength) {
        mBytesRead = pBytesRead;
        mContentLength = pContentLength;

        int progress = (int) (pBytesRead * 100 / pContentLength);
        Log.e("hhh", "progress= " + progress);
    }

    @Override
    public long getBytesRead() {
        return mBytesRead;
    }

    @Override
    public long getContentLength() {
        return mContentLength;
    }

    @Override
    public void onBackPressed() {
        if (mHttpServer.serverIsRunning) {
            exitPrompt();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHttpServer) {
            mHttpServer.stopServer();
        }

        if (null != mNetworkReceiver) {
            unregisterReceiver(mNetworkReceiver);
        }

        if (null != mReceiver) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
        }

        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back: {
                if (mHttpServer.serverIsRunning) {
                    exitPrompt();
                } else {
                    finish();
                }
            }
            break;
            case R.id.btn_setting: {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
            break;
            case R.id.btn_copy: {
                if (!TextUtils.isEmpty(mIp)) {
                    copy(mIp);
                    Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                break;
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                String action = intent.getAction();
                if (BroadcastAction.UPLOAD_WIFI_BOOK.equals(action)) {
                    String bookName = intent.getStringExtra("bookName");
                    long userId = UserInfoManager.getInstance(mContext).getUserid();
                    if (null != mPresenter) {
                        mPresenter.setWifiBook(userId, bookName);
                    }
                }
            }
        }
    };

    private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("hhh", "网络状态发生变化");
            //检测API是不是小于21，因为到了API 21之后getNetworkInfo(int networkType)方法被弃用
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                Log.i("hhh", "API level 小于21");
                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取ConnectivityManager对象对应的NetworkInfo对象
                //获取WIFI连接的信息
                NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                //获取移动数据连接的信息
                NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    Log.i("hhh", "WIFI已连接,移动数据已连接");
                    startServer();
                } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                    Log.i("hhh", "WIFI已连接,移动数据已断开");
                    startServer();
                } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    Log.i("hhh", "WIFI已断开,移动数据已连接");
                    stopServer();
                } else {
                    Log.i("hhh", "WIFI已断开,移动数据已断开");
                    stopServer();
                }
            } else {
                Log.i("hhh", "API level 大于21");
                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取所有当前已有连接上状态的网络连接的信息
                Network[] networks = connMgr.getAllNetworks();

                //用于记录最后的网络连接信息
                int result = 0;//mobile false = 1, mobile true = 2, wifi = 4

                //通过循环将网络信息逐个取出来
                for (int i = 0; i < networks.length; i++) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);

                    //检测到有数据连接，但是并连接状态未生效，此种状态为wifi和数据同时已连接，以wifi连接优先
                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && !networkInfo.isConnected()) {
                        result += 1;
                    }

                    //检测到有数据连接，并连接状态已生效，此种状态为只有数据连接，wifi并未连接上
                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected()) {
                        result += 2;
                    }

                    //检测到有wifi连接，连接状态必为true
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        result += 4;
                    }
                }

                //因为存在上述情况的组合情况，以组合相加的唯一值作为最终状态的判断
                switch (result) {
                    case 0:
                        Log.i("hhh", "WIFI已断开,移动数据已断开");
                        stopServer();
                        break;
                    case 2:
                        Log.i("hhh", "WIFI已断开,移动数据已连接");
                        stopServer();
                        break;
                    case 4:
                        startServer();
                        Log.i("hhh", "WIFI已连接,移动数据已断开");
                        break;
                    case 5:
                        Log.i("hhh", "WIFI已连接,移动数据已连接");
                        startServer();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void startServer() {
        if (null != mHttpServer && !mHttpServer.serverIsRunning) {
            mTvStatus.setText("WIFI服务已开启");
            mTvPrompt.setText("请确保您的手机和传输设备在同一个局域网内");
            // 启动wifi传书服务器
            try {
                mHttpServer.startServer();
                mIp = "http://".concat(mHttpServer.getHostname()).concat(":").concat(mHttpServer.getListeningPort() + "");
                mTvContent.setText("在电脑浏览器中访问以下地址");
                mTvContent.setTextSize(15);
                mTvWifiIp.setVisibility(View.VISIBLE);
                mTvWifiIp.setText(mIp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopServer() {
        mTvStatus.setText("WIFI服务未启动");
        mTvPrompt.setText("请确认您手机设备的连接状态");
        mBtnSetting.setVisibility(View.VISIBLE);
        mBtnCopy.setVisibility(View.GONE);

        mTvContent.setText("没有WIFI信号");
        mTvContent.setTextSize(20);
        mTvWifiIp.setVisibility(View.GONE);

        if (null != mHttpServer) {
            mHttpServer.stopServer();
        }
    }

    @Override
    public void setWifiBookSuccess() {

    }

    @Override
    public void setWifiBookFailure(String msg) {

    }
}
