package com.shangame.fiction.ui.popup;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.UserSetting;
import com.shangame.fiction.storage.model.UserInfo;

/**
 * Create by Speedy on 2018/12/24
 */
public class GoToReadPopupWindow extends BasePopupWindow implements View.OnClickListener{

    private static final String TAG = " NewUserReadRemind";

    public GoToReadPopupWindow(Activity activity) {
        super(activity);
        initView();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
    }

    private void initView() {
        View contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_go_to_read,null);
        contentView.findViewById(R.id.btnGoToRead).setOnClickListener(this);
        contentView.findViewById(R.id.ivClose).setOnClickListener(this);
        setContentView(contentView);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivClose) {
            dismiss();
        }else if(v.getId() == R.id.btnGoToRead){
            dismiss();
        }
    }
}
