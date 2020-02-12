package com.shangame.fiction.ui.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;

/**
 * Create by Speedy on 2018/12/24
 */
public class NewUserGiftPopupWindow extends BasePopupWindow implements View.OnClickListener{


    private View.OnClickListener freePermissionClickListener;

    public NewUserGiftPopupWindow(Activity activity) {
        super(activity);
        initView();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

    }

    private void initView() {
        View contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_new_user_get,null);
        Button btnGet = contentView.findViewById(R.id.btnGet);
        btnGet.setOnClickListener(this);
        ImageView ivClose = contentView.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(this);
        setContentView(contentView);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivClose){
            dismiss();
        }else if(v.getId() == R.id.btnGet){
            if(freePermissionClickListener != null){
                freePermissionClickListener.onClick(v);
                dismiss();
            }
        }
    }

    public void setFreePermissionClickListener(View.OnClickListener freePermissionClickListener) {
        this.freePermissionClickListener = freePermissionClickListener;
    }
}
