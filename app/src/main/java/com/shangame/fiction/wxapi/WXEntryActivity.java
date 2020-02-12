package com.shangame.fiction.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.manager.Logger;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";

    private IWXAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WeChatConstants.APP_ID, false);
        api.registerApp(WeChatConstants.APP_ID);

        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data,this);


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.i(TAG, "onReq: ");
    }

    @Override
    public void onResp(BaseResp baseResp) {

        String result ;

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = handleOK(baseResp);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = getString(R.string.errcode_cancel);
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result =   getString(R.string.errcode_deny);
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result =  getString(R.string.errcode_unsupported);
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                break;
            default:
                result =  getString(R.string.errcode_unknown);
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                break;
        }

        finish();
    }

    private String handleOK(BaseResp baseResp) {
        String result ;
        switch (baseResp.getType()){
            case ConstantsAPI.COMMAND_SENDAUTH:
                result = getString(R.string.login_success);
                Intent intent = new Intent();
                String code = ((SendAuth.Resp) baseResp).code;
                String state = ((SendAuth.Resp) baseResp).state;
                intent.putExtra("code",code);
                intent.putExtra("state",state);
                Log.e(TAG, "handleOK: state = "+state );
                if(WeChatConstants.State.LOGIN.equals(state)){
                    intent.setAction(BroadcastAction.WECHAT_LOGION_ACTION);
                }else if(WeChatConstants.State.BIND.equals(state)){
                    intent.setAction(BroadcastAction.WECHAT_BIND_ACTION);
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                break;
            case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                result = getString(R.string.share_success);
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastAction.SHARE_TO_WECHAT_SUCCESS_ACTION));
                break;
             default:
                 result="";
        }
        return result;
    }


}
