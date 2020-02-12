package com.shangame.fiction.ui.wifi.nanohttpd;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;

public class DeviceInfoDispatcher extends BaseContextDispatcher {

    public DeviceInfoDispatcher(Context context) {
        super(context);
    }

    @Override
    public Response handle(IHTTPSession session) {
        return Response.newFixedLengthResponse(Status.OK, "application/json", getDeviceInfo(getWiFiName()));
    }

    private String getWiFiName() {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            return wifiInfo.getSSID();
        }
        return null;
    }

    private String getDeviceInfo(String networkName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_desc", "当前网络：" + networkName + "</br>（传书过程中，请不要关闭电子书上的WiFi传书窗口。）");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
