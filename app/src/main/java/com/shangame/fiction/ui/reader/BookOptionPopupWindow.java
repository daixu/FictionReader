package com.shangame.fiction.ui.reader;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.storage.manager.UserSetting;

/**
 * Create by Speedy on 2018/8/15
 */
public class BookOptionPopupWindow extends BasePopupWindow implements View.OnClickListener {

    private Callback callback;
    private UserSetting setting;
    private View ivDot;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public BookOptionPopupWindow(Activity activity) {
        super(activity);
        initView();
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundAlpha(1f);
    }

    private void initView() {
        contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_book_option, null);
        setContentView(contentView);
        contentView.findViewById(R.id.tvError).setOnClickListener(this);
        contentView.findViewById(R.id.tvAddToBookrack).setOnClickListener(this);
        contentView.findViewById(R.id.tvShare).setOnClickListener(this);

        ivDot = contentView.findViewById(R.id.ivDot);

        setting = UserSetting.getInstance(mActivity);
        boolean hasShareRead = setting.getBoolean(SharedKey.HAS_SHARE_READ, false);
        if (hasShareRead) {
            ivDot.setVisibility(View.GONE);
        } else {
            ivDot.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.tvAddToBookrack:
                callback.addBookMark();
                break;
            case R.id.tvError:
                callback.toError();
                break;
            case R.id.tvShare:
                setting.putBoolean(SharedKey.HAS_SHARE_READ, true);
                ivDot.setVisibility(View.GONE);
                callback.toShare();
                break;
            default:
                break;
        }
    }

    public interface Callback {
        void addBookMark();

        void toShare();

        void toError();
    }
}
