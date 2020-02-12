package com.shangame.fiction.ui.reader;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.book.config.PageConfig;
import com.shangame.fiction.book.helper.FontHelper;
import com.shangame.fiction.book.helper.PaintHelper;
import com.shangame.fiction.core.base.BasePopupWindow;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;

/**
 * Create by Speedy on 2018/8/14
 */
public class PageSettingPopupWindow extends BasePopupWindow implements View.OnClickListener {

    private SettingCallback settingCallback;

    private int fontSizeIndex = 3;
    private TextView tvFontSizeIndex;
    private SeekBar lightSeekBar;

    private ImageView ivSpace1;
    private ImageView ivSpace2;
    private ImageView ivSpace3;

    private ImageView ivBg1;
    private ImageView ivBg2;
    private ImageView ivBg3;
    private ImageView ivBg4;
    private ImageView ivBg5;

    private PageConfig mPageConfig;

    public PageSettingPopupWindow(Activity activity) {
        super(activity);
        initView();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.popup_anim_style);
        setBackgroundAlpha(1.0f);
    }

    private void initView() {
        contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_page_setting, null);
        setContentView(contentView);
        lightSeekBar = contentView.findViewById(R.id.lightSeekBar);
        int brightNess = AppSetting.getInstance(mActivity).getInt(SharedKey.ACTIVITY_BRIGHTNESS, 50);
        lightSeekBar.setProgress(brightNess);
        lightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (settingCallback != null && fromUser) {
                    settingCallback.setActivityLight(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        contentView.findViewById(R.id.ivFontDown).setOnClickListener(this);
        contentView.findViewById(R.id.ivFontUp).setOnClickListener(this);
        tvFontSizeIndex = contentView.findViewById(R.id.tvFontSizeIndex);

        ivBg1 = contentView.findViewById(R.id.ivBg1);
        ivBg2 = contentView.findViewById(R.id.ivBg2);
        ivBg3 = contentView.findViewById(R.id.ivBg3);
        ivBg4 = contentView.findViewById(R.id.ivBg4);
        ivBg5 = contentView.findViewById(R.id.ivBg5);

        ivBg1.setOnClickListener(this);
        ivBg2.setOnClickListener(this);
        ivBg3.setOnClickListener(this);
        ivBg4.setOnClickListener(this);
        ivBg5.setOnClickListener(this);

        ivSpace1 = contentView.findViewById(R.id.ivSpace1);
        ivSpace1.setOnClickListener(this);

        ivSpace2 = contentView.findViewById(R.id.ivSpace2);
        ivSpace2.setOnClickListener(this);

        ivSpace3 = contentView.findViewById(R.id.ivSpace3);
        ivSpace3.setOnClickListener(this);
    }

    public void initConfig(PageConfig pageConfig) {
        mPageConfig = pageConfig;
        lightSeekBar.setProgress(mPageConfig.sceenLight);
        switch (mPageConfig.fontSize) {
            case PageConfig.FontSize.SIZE_1:
                fontSizeIndex = 1;
                break;
            case PageConfig.FontSize.SIZE_2:
                fontSizeIndex = 2;
                break;
            case PageConfig.FontSize.SIZE_3:
                fontSizeIndex = 3;
                break;
            case PageConfig.FontSize.SIZE_4:
                fontSizeIndex = 4;
                break;
            case PageConfig.FontSize.SIZE_5:
                fontSizeIndex = 5;
                break;
            case PageConfig.FontSize.SIZE_6:
                fontSizeIndex = 6;
                break;
            case PageConfig.FontSize.SIZE_7:
                fontSizeIndex = 7;
                break;
            default:
                break;
        }
        tvFontSizeIndex.setText(String.valueOf(fontSizeIndex));
        setLineSpaceImage(mPageConfig.lineSpace);
        setBgImage(mPageConfig.backgroundColor);
    }

    private void setLineSpaceImage(int lineSpace) {
        switch (mPageConfig.lineSpace) {
            case PageConfig.LineSpace.SPACE_12:
                ivSpace1.setImageResource(R.drawable.space_1_n);
                ivSpace2.setImageResource(R.drawable.space_2_n);
                ivSpace3.setImageResource(R.drawable.space_3_s);
                break;
            case PageConfig.LineSpace.SPACE_18:
                ivSpace1.setImageResource(R.drawable.space_1_n);
                ivSpace2.setImageResource(R.drawable.space_2_s);
                ivSpace3.setImageResource(R.drawable.space_3_n);
                break;
            case PageConfig.LineSpace.SPACE_24:
                ivSpace1.setImageResource(R.drawable.space_1_s);
                ivSpace2.setImageResource(R.drawable.space_2_n);
                ivSpace3.setImageResource(R.drawable.space_3_n);
                break;
            default:
                break;
        }
    }

    private void setBgImage(int backgroundColor) {
        switch (backgroundColor) {
            case PageConfig.BackgroundColor.COLOR_1:
                ivBg1.setSelected(true);
                ivBg2.setSelected(false);
                ivBg3.setSelected(false);
                ivBg4.setSelected(false);
                ivBg5.setSelected(false);
                break;
            case PageConfig.BackgroundColor.COLOR_2:
                ivBg1.setSelected(false);
                ivBg2.setSelected(true);
                ivBg3.setSelected(false);
                ivBg4.setSelected(false);
                ivBg5.setSelected(false);
                break;
            case PageConfig.BackgroundColor.COLOR_3:
                ivBg1.setSelected(false);
                ivBg2.setSelected(false);
                ivBg3.setSelected(true);
                ivBg4.setSelected(false);
                ivBg5.setSelected(false);
                break;
            case PageConfig.BackgroundColor.COLOR_4:
                ivBg1.setSelected(false);
                ivBg2.setSelected(false);
                ivBg3.setSelected(false);
                ivBg4.setSelected(true);
                ivBg5.setSelected(false);
                break;
            default:
                ivBg1.setSelected(false);
                ivBg2.setSelected(false);
                ivBg3.setSelected(false);
                ivBg4.setSelected(false);
                ivBg5.setSelected(true);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivFontDown:
                fontSizeIndex--;
                if (fontSizeIndex < 1) {
                    fontSizeIndex = 1;
                    showToast("已经是最小字体");
                    return;
                }
                tvFontSizeIndex.setText(String.valueOf(fontSizeIndex));
                setFontSize(fontSizeIndex);
                break;
            case R.id.ivFontUp:
                fontSizeIndex++;
                if (fontSizeIndex > FontHelper.MAX_FONT_INDEX) {
                    fontSizeIndex = FontHelper.MAX_FONT_INDEX;
                    showToast("已经是最大字体");
                    return;
                }
                tvFontSizeIndex.setText(String.valueOf(fontSizeIndex));
                setFontSize(fontSizeIndex);
                break;
            case R.id.ivBg1:
                settingCallback.setBackgroud(PageConfig.BackgroundColor.COLOR_1);
                ivBg1.setSelected(true);
                ivBg2.setSelected(false);
                ivBg3.setSelected(false);
                ivBg4.setSelected(false);
                ivBg5.setSelected(false);
                break;
            case R.id.ivBg2:
                settingCallback.setBackgroud(PageConfig.BackgroundColor.COLOR_2);
                ivBg1.setSelected(false);
                ivBg2.setSelected(true);
                ivBg3.setSelected(false);
                ivBg4.setSelected(false);
                ivBg5.setSelected(false);
                break;
            case R.id.ivBg3:
                settingCallback.setBackgroud(PageConfig.BackgroundColor.COLOR_3);
                ivBg1.setSelected(false);
                ivBg2.setSelected(false);
                ivBg3.setSelected(true);
                ivBg4.setSelected(false);
                ivBg5.setSelected(false);
                break;
            case R.id.ivBg4:
                settingCallback.setBackgroud(PageConfig.BackgroundColor.COLOR_4);
                ivBg1.setSelected(false);
                ivBg2.setSelected(false);
                ivBg3.setSelected(false);
                ivBg4.setSelected(true);
                ivBg5.setSelected(false);
                break;
            case R.id.ivBg5:
                settingCallback.setBackgroud(PageConfig.BackgroundColor.COLOR_5);
                ivBg1.setSelected(false);
                ivBg2.setSelected(false);
                ivBg3.setSelected(false);
                ivBg4.setSelected(false);
                ivBg5.setSelected(true);
                break;
            case R.id.ivSpace1:
                PaintHelper.clear();
                settingCallback.setLineSpace(PageConfig.LineSpace.SPACE_24);
                setLineSpaceImage(PageConfig.LineSpace.SPACE_24);
                break;
            case R.id.ivSpace2:
                PaintHelper.clear();
                settingCallback.setLineSpace(PageConfig.LineSpace.SPACE_18);
                setLineSpaceImage(PageConfig.LineSpace.SPACE_18);
                break;
            case R.id.ivSpace3:
                PaintHelper.clear();
                settingCallback.setLineSpace(PageConfig.LineSpace.SPACE_12);
                setLineSpaceImage(PageConfig.LineSpace.SPACE_12);
                break;
            default:
                break;
        }
    }

    public void setFontSize(int index) {
        PaintHelper.clear();
        switch (index) {
            case 1:
                settingCallback.setFontSize(PageConfig.FontSize.SIZE_1);
                break;
            case 2:
                settingCallback.setFontSize(PageConfig.FontSize.SIZE_2);
                break;
            case 3:
                settingCallback.setFontSize(PageConfig.FontSize.SIZE_3);
                break;
            case 4:
                settingCallback.setFontSize(PageConfig.FontSize.SIZE_4);
                break;
            case 5:
                settingCallback.setFontSize(PageConfig.FontSize.SIZE_5);
                break;
            case 6:
                settingCallback.setFontSize(PageConfig.FontSize.SIZE_6);
                break;
            case 7:
                settingCallback.setFontSize(PageConfig.FontSize.SIZE_7);
                break;
            default:
                break;
        }
    }

    public void setSettingCallback(SettingCallback settingCallback) {
        this.settingCallback = settingCallback;
    }

    public interface SettingCallback {
        void setActivityLight(int light);

        void setBackgroud(int color);

        void setLineSpace(int space);

        void setFontSize(int size);
    }
}
