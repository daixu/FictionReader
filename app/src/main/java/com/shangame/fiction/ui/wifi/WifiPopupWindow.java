package com.shangame.fiction.ui.wifi;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.core.BottomPopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.core.utils.NetworkUtils;
import com.shangame.fiction.ui.wifi.nanohttpd.DeviceInfoDispatcher;
import com.shangame.fiction.ui.wifi.nanohttpd.HttpServer;
import com.shangame.fiction.ui.wifi.nanohttpd.LongPollingDispatcher;
import com.shangame.fiction.ui.wifi.nanohttpd.ResourceDispatcher;
import com.shangame.fiction.ui.wifi.nanohttpd.UploadFileDispatcher;
import com.shangame.fiction.ui.wifi.nanohttpd.UploadFileProgressDispathcer;

import org.nanohttpd.protocols.http.progress.ProgressListener;
import org.nanohttpd.protocols.http.request.Method;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * WIFI传书弹出框
 *
 * @author hhh
 */
public class WifiPopupWindow extends BottomPopupView implements ProgressListener {

    private Context mContext;
    private String mIp;

    private long mBytesRead;
    private long mContentLength;

    private TextView mTvContent;
    private TextView mTvWifiIp;
    private Button mBtnCopy;
    private Button mBtnSetting;
    private TextView mTvStatus;
    private TextView mTvPrompt;
    private ImageView mImgSuccess;
    private TextView mTvSuccess;
    private TextView mTextUploadTotal;

    private HttpServer mHttpServer;
    private WifiHandler mHandler;
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

    public WifiPopupWindow(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_wifi;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initServer();
        initData();
        initListener();
        initBroadcast();
    }

    private void initView() {
        mTvContent = findViewById(R.id.tv_content);
        mTvWifiIp = findViewById(R.id.tv_wifi_ip);
        mBtnSetting = findViewById(R.id.btn_setting);
        mBtnCopy = findViewById(R.id.btn_copy);
        mTvStatus = findViewById(R.id.tv_status);
        mTvPrompt = findViewById(R.id.tv_prompt);

        mImgSuccess = findViewById(R.id.img_success);
        mTvSuccess = findViewById(R.id.tv_success);
        mTextUploadTotal = findViewById(R.id.text_upload_total);

        mHandler = new WifiHandler(this);
    }

    private void initServer() {
        mHttpServer = new HttpServer(9093, this);
        mHttpServer.register(Method.GET, "/", new ResourceDispatcher(mContext))
                .register(Method.GET, "/images/.*", new ResourceDispatcher(mContext))
                .register(Method.GET, "/scripts/.*", new ResourceDispatcher(mContext))
                .register(Method.GET, "/css/.*", new ResourceDispatcher(mContext))
                .register(Method.GET, "/imgs/.*", new ResourceDispatcher(mContext))
                .register(Method.GET, "/js/.*", new ResourceDispatcher(mContext))
                .register(Method.GET, "/getDeviceInfo", new DeviceInfoDispatcher(mContext))
                .register(Method.POST, "/upload", new UploadFileDispatcher())
                .register(Method.POST, "/files", new UploadFileDispatcher())
                .register(Method.GET, "/upload", new UploadFileProgressDispathcer(this))
                .register(Method.POST, "/longpolling", new LongPollingDispatcher());
    }

    public void initData() {
        String wifiIp = NetworkUtils.getConnectWifiIp(mContext);
        if (!TextUtils.isEmpty(wifiIp)) {
            mBtnSetting.setVisibility(View.GONE);
            mBtnCopy.setVisibility(View.VISIBLE);
            mTvStatus.setText("WIFI服务已开启");
            mTvPrompt.setText("请确保您的手机和传输设备在同一个局域网内");

            mImgSuccess.setVisibility(View.GONE);
            mTvSuccess.setVisibility(View.GONE);
            mTextUploadTotal.setVisibility(View.GONE);
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

            mImgSuccess.setVisibility(View.GONE);
            mTvSuccess.setVisibility(View.GONE);
            mTextUploadTotal.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        findViewById(R.id.btn_copy).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mIp)) {
                    copy(mIp);
                    Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.tv_exit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        findViewById(R.id.btn_setting).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                mContext.startActivity(intent);
            }
        });
    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mNetworkReceiver, filter);
    }

    private void copy(String copyStr) {
        //获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", copyStr);
        // 将ClipData内容放到系统剪贴板里。
        if (null != cm) {
            cm.setPrimaryClip(mClipData);
        }
    }

    /**
     * 完全消失执行
     */
    @Override
    protected void onDismiss() {
        if (null != mHttpServer) {
            mHttpServer.stopServer();
        }

        if (null != mNetworkReceiver) {
            mContext.unregisterReceiver(mNetworkReceiver);
        }
    }

    /**
     * 完全可见执行
     */
    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    public void update(long pBytesRead, long pContentLength) {
        mBytesRead = pBytesRead;
        mContentLength = pContentLength;

        int progress = (int) (pBytesRead * 100 / pContentLength);
        Log.e("hhh", "progress= " + progress);

        if (progress == 100) {
            int fileTotal = WifiBean.getInstance().fileTotal;
            Log.e("hhh", "fileTotal= " + fileTotal);

            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            msg.arg1 = fileTotal;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public long getBytesRead() {
        return mBytesRead;
    }

    @Override
    public long getContentLength() {
        return mContentLength;
    }

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

                mBtnSetting.setVisibility(View.GONE);
                mBtnCopy.setVisibility(View.VISIBLE);

                mImgSuccess.setVisibility(View.GONE);
                mTvSuccess.setVisibility(View.GONE);
                mTextUploadTotal.setVisibility(View.GONE);
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

        mImgSuccess.setVisibility(View.GONE);
        mTvSuccess.setVisibility(View.GONE);
        mTextUploadTotal.setVisibility(View.GONE);

        if (null != mHttpServer) {
            mHttpServer.stopServer();
        }
    }

    private static class WifiHandler extends Handler {

        private WeakReference<WifiPopupWindow> mRef;

        public WifiHandler(WifiPopupWindow view) {
            mRef = new WeakReference(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int fileTotal = msg.arg1;
                WifiPopupWindow mView = this.mRef.get();

                mView.mTvContent.setVisibility(View.GONE);
                mView.mTvWifiIp.setVisibility(View.GONE);
                mView.mBtnCopy.setVisibility(View.GONE);
                mView.mBtnSetting.setVisibility(View.GONE);

                mView.mImgSuccess.setVisibility(View.VISIBLE);
                mView.mTvSuccess.setVisibility(View.VISIBLE);
                mView.mTextUploadTotal.setVisibility(View.VISIBLE);

                mView.mTextUploadTotal.setText(mView.mContext.getString(R.string.wifi_upload_file_total, fileTotal));
            }
        }
    }
}
